package lc.activiti.dao.base;

import lc.activiti.entity.BrowsePermission;

public interface IBrowsePermissionBaseDao {
    int deleteByPrimaryKey(String browsePermissionId);

    int insert(BrowsePermission record);

    int insertSelective(BrowsePermission record);

    BrowsePermission selectByPrimaryKey(String browsePermissionId);

    int updateByPrimaryKeySelective(BrowsePermission record);

    int updateByPrimaryKey(BrowsePermission record);
}