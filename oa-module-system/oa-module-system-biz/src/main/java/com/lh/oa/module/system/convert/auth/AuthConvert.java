package com.lh.oa.module.system.convert.auth;

import com.lh.oa.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthMenuRespVO;
import com.lh.oa.module.system.controller.admin.auth.vo.AuthPermissionInfoRespVO;
import com.lh.oa.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.lh.oa.module.system.dal.dataobject.permission.MenuDO;
import com.lh.oa.module.system.dal.dataobject.permission.RoleDO;
import com.lh.oa.module.system.dal.dataobject.user.AdminUserDO;
import org.apache.commons.lang3.ObjectUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertMap;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.convertSet;
import static com.lh.oa.framework.common.util.collection.CollectionUtils.filterList;

@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AuthLoginRespVO convert(OAuth2AccessTokenDO bean);

    default AuthPermissionInfoRespVO convert(AdminUserDO user, List<RoleDO> roleList, List<MenuDO> menuList) {
        convertMap(roleList,RoleDO::getId,RoleDO::getCode);
        return AuthPermissionInfoRespVO.builder()
                .user(AuthPermissionInfoRespVO.UserVO.builder().id(user.getId()).nickname(user.getNickname()).bankAccount(ObjectUtils.isNotEmpty(user.getBankAccount()) ? user.getBankAccount() : "无").bankAccountNumber(ObjectUtils.isNotEmpty(user.getBankAccountNumber()) ? user.getBankAccountNumber() : "无").avatar(user.getAvatar()).deptId(user.getDeptId()).postId((Long) user.getPostIds().toArray()[0]).build())
                .roles(convertSet(roleList, RoleDO::getCode))
                .permissions(convertSet(menuList, MenuDO::getPermission))
                .build();
    }

    default AuthMenuRespVO convertTreeNode(MenuDO menu) {
        if (menu == null) {

            return null;
        }

        AuthMenuRespVO.AuthMenuRespVOBuilder authMenuRespVO = AuthMenuRespVO.builder();
        AuthMenuRespVO.Meta.MetaBuilder builder = AuthMenuRespVO.Meta.builder();
        builder.icon(menu.getIcon());
        builder.title(menu.getName());
        builder.ignoreKeepAlive(menu.getKeepAlive());
        builder.hideMenu(menu.getVisible());
        builder.hideTab(menu.getAlwaysShow());
        builder.currentActiveMenu(menu.getCurrentActiveMenu());
        authMenuRespVO.id(menu.getId());
        authMenuRespVO.parentId(menu.getParentId());
        authMenuRespVO.name(menu.getName());
        authMenuRespVO.path(menu.getPath());
        authMenuRespVO.component(menu.getComponent());
        authMenuRespVO.componentName(menu.getComponentName());
        authMenuRespVO.icon(menu.getIcon());
        authMenuRespVO.visible(menu.getVisible());
        authMenuRespVO.keepAlive(menu.getKeepAlive());
        authMenuRespVO.alwaysShow(menu.getAlwaysShow());
        authMenuRespVO.meta(builder.build());
        authMenuRespVO.extend(menu.getExtend());


        return authMenuRespVO.build();
    }

    /**
     * 将菜单列表，构建成菜单树
     *
     * @param menuList 菜单列表
     * @return 菜单树
     */
    default List<AuthMenuRespVO> buildMenuTree(List<MenuDO> menuList) {
        // 排序，保证菜单的有序性
        menuList.sort(Comparator.comparing(MenuDO::getSort));
        // 构建菜单树
        Map<Long, AuthMenuRespVO> treeNodeMap = new LinkedHashMap<>();
        menuList.forEach(menu -> treeNodeMap.put(menu.getId(), AuthConvert.INSTANCE.convertTreeNode(menu)));
        // 处理父子关系
        treeNodeMap.values().stream().filter(node -> !node.getParentId().equals(MenuDO.ID_ROOT)).forEach(childNode -> {
            // 获得父节点
            AuthMenuRespVO parentNode = treeNodeMap.get(childNode.getParentId());
            if (parentNode == null) {
                LoggerFactory.getLogger(getClass()).error("[buildRouterTree][resource({}) 找不到父资源({})]",
                        childNode.getId(), childNode.getParentId());
                return;
            }
            // 将自己添加到父节点中
            if (parentNode.getChildren() == null) {
                parentNode.setChildren(new ArrayList<>());
            }
            parentNode.getChildren().add(childNode);
        });
        // 获得到所有的根节点
        return filterList(treeNodeMap.values(), node -> MenuDO.ID_ROOT.equals(node.getParentId()));
    }

}
