package com.mark.aclservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mark.aclservice.entity.Permission;
import com.mark.aclservice.entity.RolePermission;
import com.mark.aclservice.entity.User;
import com.mark.aclservice.entity.vo.PerMenusVO;
import com.mark.aclservice.helper.MemuHelper;
import com.mark.aclservice.helper.PermissionHelper;
import com.mark.aclservice.mapper.PermissionMapper;
import com.mark.aclservice.service.PermissionService;
import com.mark.aclservice.service.RolePermissionService;
import com.mark.aclservice.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.xmlbeans.impl.regex.REUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 权限 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private RolePermissionService rolePermissionService;
    
    @Autowired
    private UserService userService;
    
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenu() {

        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);

        List<Permission> result = bulid(permissionList);

        return result;
    }

    //根据角色获取菜单
    @Override
    public List<Permission> selectAllMenu(String roleId) {
        List<Permission> allPermissionList = baseMapper.selectList(new QueryWrapper<Permission>().orderByAsc("CAST(id AS SIGNED)"));

        //根据角色id获取角色权限
        List<RolePermission> rolePermissionList = rolePermissionService.list(new QueryWrapper<RolePermission>().eq("role_id",roleId));
        //转换给角色id与角色权限对应Map对象
//        List<String> permissionIdList = rolePermissionList.stream().map(e -> e.getPermissionId()).collect(Collectors.toList());
//        allPermissionList.forEach(permission -> {
//            if(permissionIdList.contains(permission.getId())) {
//                permission.setSelect(true);
//            } else {
//                permission.setSelect(false);
//            }
//        });
        for (int i = 0; i < allPermissionList.size(); i++) {
            Permission permission = allPermissionList.get(i);
            for (int m = 0; m < rolePermissionList.size(); m++) {
                RolePermission rolePermission = rolePermissionList.get(m);
                if(rolePermission.getPermissionId().equals(permission.getId())) {
                    permission.setSelect(true);
                }
            }
        }

        List<Permission> permissionList = bulid(allPermissionList);
        return permissionList;
    }

    //给角色分配权限
    @Override
    public void saveRolePermissionRealtionShip(String roleId, String[] permissionIds) {

        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        List<RolePermission> rolePermissionList = new ArrayList<>();
        for(String permissionId : permissionIds) {
            if(StringUtils.isEmpty(permissionId)) {
                continue;
            }
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionList.add(rolePermission);
        }
        rolePermissionService.saveBatch(rolePermissionList);
    }

    //递归删除菜单
    @Override
    public void removeChildById(String id) {
        List<String> idList = new ArrayList<>();
        this.selectChildListById(id, idList);

        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //根据用户id获取用户菜单
    @Override
    public List<String> selectPermissionValueByUserId(String id) {

        List<String> selectPermissionValueList = null;
        if(this.isSysAdmin(id)) {
            //如果是系统管理员，获取所有权限
            selectPermissionValueList = baseMapper.selectAllPermissionValue();
        } else {
            selectPermissionValueList = baseMapper.selectPermissionValueByUserId(id);
        }
        return selectPermissionValueList;
    }

    @Override
    public List<JSONObject> selectPermissionByUserId(String userId) {
        List<Permission> selectPermissionList = null;
        if(this.isSysAdmin(userId)) {
            //如果是超级管理员，获取所有菜单
            selectPermissionList = baseMapper.selectList(null);
        } else {
            selectPermissionList = baseMapper.selectPermissionByUserId(userId);
        }

        List<Permission> permissionList = PermissionHelper.bulid(selectPermissionList);
        List<JSONObject> result = MemuHelper.bulid(permissionList);
        return result;
    }

    /**
     * 判断用户是否系统管理员
     * @param userId
     * @return
     */
    private boolean isSysAdmin(String userId) {
        User user = userService.getById(userId);

        if(null != user && "admin".equals(user.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     *	递归获取子节点
     * @param id
     * @param idList
     */
    private void selectChildListById(String id, List<String> idList) {
        List<Permission> childList = baseMapper.selectList(new QueryWrapper<Permission>().eq("pid", id).select("id"));
        childList.stream().forEach(item -> {
            idList.add(item.getId());
            this.selectChildListById(item.getId(), idList);
        });
    }

    /**
     * 使用递归方法建菜单
     * @param treeNodes
     * @return
     */
    private static List<Permission> bulid(List<Permission> treeNodes) {
        List<Permission> trees = new ArrayList<>();
        for (Permission treeNode : treeNodes) {
            if ("0".equals(treeNode.getPid())) {
                treeNode.setLevel(1);
                trees.add(findChildren(treeNode,treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     * @return
     */
    private static Permission findChildren(Permission treeNode,List<Permission> treeNodes) {
        treeNode.setChildren(new ArrayList<Permission>());

        for (Permission it : treeNodes) {
            if(treeNode.getId().equals(it.getPid())) {
                int level = treeNode.getLevel() + 1;
                it.setLevel(level);
                if (treeNode.getChildren() == null) {
                    treeNode.setChildren(new ArrayList<>());
                }
                treeNode.getChildren().add(findChildren(it,treeNodes));
            }
        }
        return treeNode;
    }


    //========================递归查询所有菜单================================================
    //获取全部菜单
    @Override
    public List<Permission> queryAllMenuGuli() {
        //1 查询菜单表所有数据
        QueryWrapper<Permission> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");
        List<Permission> permissionList = baseMapper.selectList(wrapper);
        //2 把查询所有菜单list集合按照要求进行封装
        List<Permission> resultList = bulidPermission(permissionList);
        return resultList;
    }

    //把返回所有菜单list集合进行封装的方法
    public static List<Permission> bulidPermission(List<Permission> permissionList) {

        //创建list集合，用于数据最终封装
        List<Permission> finalNode = new ArrayList<>();
        //把所有菜单list集合遍历，得到顶层菜单 pid=0菜单，设置level是1
        for(Permission permissionNode : permissionList) {
            //得到顶层菜单 pid=0菜单
            if("0".equals(permissionNode.getPid())) {
                //设置顶层菜单的level是1
                permissionNode.setLevel(1);
                //根据顶层菜单，向里面进行查询子菜单，封装到finalNode里面
                finalNode.add(selectChildren(permissionNode,permissionList));
            }
        }
        return finalNode;
    }

    private static Permission selectChildren(Permission permissionNode, List<Permission> permissionList) {
        //1 因为向一层菜单里面放二层菜单，二层里面还要放三层，把对象初始化
        permissionNode.setChildren(new ArrayList<Permission>());

        //2 遍历所有菜单list集合，进行判断比较，比较id和pid值是否相同
        for(Permission it : permissionList) {
            //判断 id和pid值是否相同
            if(permissionNode.getId().equals(it.getPid())) {
                //把父菜单的level值+1
                int level = permissionNode.getLevel()+1;
                it.setLevel(level);
                //如果children为空，进行初始化操作
                if(permissionNode.getChildren() == null) {
                    permissionNode.setChildren(new ArrayList<Permission>());
                }
                //把查询出来的子菜单放到父菜单里面
                permissionNode.getChildren().add(selectChildren(it,permissionList));
            }
        }
        return permissionNode;
    }

    //============递归删除菜单==================================
    @Override
    public void removeChildByIdGuli(String id) {
        //1 创建list集合，用于封装所有删除菜单id值
        List<String> idList = new ArrayList<>();
        //2 向idList集合设置删除菜单id
        this.selectPermissionChildById(id,idList);
        //把当前id封装到list里面
        idList.add(id);
        baseMapper.deleteBatchIds(idList);
    }

    //2 根据当前菜单id，查询菜单里面子菜单id，封装到list集合
    private void selectPermissionChildById(String id, List<String> idList) {
        //查询菜单里面子菜单id
        QueryWrapper<Permission>  wrapper = new QueryWrapper<>();
        wrapper.eq("pid",id);
        wrapper.select("id");
        List<Permission> childIdList = baseMapper.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getId());
            //递归查询
            this.selectPermissionChildById(item.getId(),idList);
        });
    }

    //=========================给角色分配菜单=======================
    @Override
    public void saveRolePermissionRealtionShipGuli(String roleId, String[] permissionIds) {
        // 先删除该角色原有的权限信息
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", roleId));

        //roleId角色id
        //permissionId菜单id 数组形式
        //1 创建list集合，用于封装添加数据
        List<RolePermission> rolePermissionList = new ArrayList<>();
        //遍历所有菜单数组
        for(String perId : permissionIds) {
            //RolePermission对象
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(perId);
            //封装到list集合
            rolePermissionList.add(rolePermission);
        }
        //添加到角色菜单关系表
        rolePermissionService.saveBatch(rolePermissionList);
    }

    /**
     * 获取全部菜单数据
     * @return List<PerMenusVO>
     */
    @Override
    public List<PerMenusVO> getAllMenu() {
        // 查询所有菜单数据
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        List<Permission> permissions = baseMapper.selectList(queryWrapper);
        // 初始化层级
        int level = 0;
        return getChildMenus("0", permissions, level);
    }

    /**
     * 获取所有下级菜单数据
     * @param id 父级菜单id
     * @param permissions 所有菜单数据
     * @return List<PerMenusVO>
     */
    private List<PerMenusVO> getChildMenus(String id, List<Permission> permissions, int level) {
        // 叠加层级
        level++;
        // 构建下级菜单集合
        List<PerMenusVO> childMenus = new ArrayList<>();
        // 遍历所有菜单数据
        for (Permission permission : permissions) {
            // 判断父级id与下级的pid
            if (id.equals(permission.getPid())) {
                // 构建返回菜单对象
                PerMenusVO perMenusVO = new PerMenusVO();
                // 设置属性
                BeanUtils.copyProperties(permission, perMenusVO);
                // 设置层级
                perMenusVO.setLevel(level);
                // 使用递归，设置下级菜单
                perMenusVO.setChildren(getChildMenus(permission.getId(), permissions, level));
                // 封装到下级菜单集合中
                childMenus.add(perMenusVO);
            }
        }
        return childMenus;
    }

    /**
     * 删除级联菜单
     * @param id 上层菜单id
     */
    @Override
    public void deleteMenu(String id) {
        // 查询所有菜单数据
        QueryWrapper<Permission> permissionWrapper = new QueryWrapper<>();
        permissionWrapper.select("id", "pid");
        List<Permission> permissions = baseMapper.selectList(permissionWrapper);
        // 构建最终的封装结果
        List<String> menus = new ArrayList<>();
        // 获取上级菜单下的所有下级菜单id集合
        getCascadeMenu(id, permissions, menus);
        // 添加上级菜单id
        menus.add(id);
        // 删除菜单
        baseMapper.deleteBatchIds(menus);
    }

    /**
     * 递归获取菜单id
     * @param id 上级菜单id
     * @param permissions 所有菜单数据
     * @return List<String>
     */
    private void getCascadeMenu(String id, List<Permission> permissions, List<String> menus) {
        // 遍历所有菜单数据
        for (Permission permission : permissions) {
            // 判断上级菜单id和下级菜单的pid
            if (id.equals(permission.getPid())) {
                menus.add(permission.getId());
                // 递归查询该层级菜单下的子菜单
                getCascadeMenu(permission.getId(), permissions, menus);
            }
        }
    }

    /**
     * 给角色分配权限
     * @param rid 角色id
     * @param aid 权限id数组
     */
    @Override
    public void roleAllocationAuthority(String rid, String[] aid) {
        // 移除角色原有的权限
        rolePermissionService.remove(new QueryWrapper<RolePermission>().eq("role_id", rid));
        // 构建角色权限集合
        List<RolePermission> rolePermissions = new ArrayList<>();
        // 遍历权限id数组
        for (String pid : aid) {
            // 构建角色权限对象
            RolePermission rolePermission = new RolePermission();
            // 设置角色id
            rolePermission.setRoleId(rid);
            // 设置权限id
            rolePermission.setPermissionId(pid);
            // 封装到集合中
            rolePermissions.add(rolePermission);
        }
        // 执行添加
        rolePermissionService.saveBatch(rolePermissions);
    }
}
