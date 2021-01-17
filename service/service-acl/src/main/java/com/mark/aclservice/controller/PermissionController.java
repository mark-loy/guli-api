package com.mark.aclservice.controller;


import com.mark.aclservice.entity.Permission;
import com.mark.aclservice.entity.vo.PerMenusVO;
import com.mark.aclservice.service.PermissionService;
import com.mark.commonutil.entity.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Api(value = "菜单管理", tags = {"菜单服务接口"})
@RestController
@RequestMapping("/admin/acl/permission")
public class PermissionController {

    @Resource
    private PermissionService permissionService;

    //获取全部菜单
    @ApiOperation(value = "查询所有菜单")
    @GetMapping
    public Result indexAllPermission() {
        List<Permission> list =  permissionService.queryAllMenuGuli();
        return Result.ok().data("children",list);
    }

    @ApiOperation(value = "递归删除菜单")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable String id) {
        permissionService.removeChildByIdGuli(id);
        return Result.ok();
    }

    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result doAssign(String roleId,String[] permissionId) {
        permissionService.saveRolePermissionRealtionShipGuli(roleId,permissionId);
        return Result.ok();
    }

    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result toAssign(@PathVariable String roleId) {
        List<Permission> list = permissionService.selectAllMenu(roleId);
        return Result.ok().data("children", list);
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("save")
    public Result save(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok();
    }

    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result updateById(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok();
    }

    /**
     * 获取所有菜单信息
     * @return Result
     */
    @ApiOperation(value = "获取所有菜单信息")
    @GetMapping("/all/menus")
    public Result getMenuAll() {
        List<PerMenusVO> perMenus = permissionService.getAllMenu();
        return Result.ok().data("menus", perMenus);
    }

    /**
     * 根据id删除菜单及子菜单
     * @param id 菜单id
     * @return Result
     */
    @ApiOperation("根据id删除菜单及子菜单")
    @DeleteMapping("delete/{id}")
    public Result deleteMenu(@ApiParam("菜单id") @PathVariable("id") String id) {
        permissionService.deleteMenu(id);
        return Result.ok();
    }

    /**
     * 给角色分配权限
     * @param rid 角色id
     * @param aid 权限id数组
     * @return Result
     */
    @ApiOperation("给角色分配权限")
    @GetMapping("/role/{rid}/{aid}")
    public Result roleAllocationAuthority(@ApiParam("角色id") @PathVariable("rid") String rid,
                                          @ApiParam("权限id数组") @PathVariable("aid") String[] aid) {
        permissionService.roleAllocationAuthority(rid, aid);
        return Result.ok();
    }
}

