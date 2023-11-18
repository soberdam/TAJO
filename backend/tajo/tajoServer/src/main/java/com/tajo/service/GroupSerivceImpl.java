package com.tajo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tajo.dao.GroupDao;
import com.tajo.dto.Group;

public class GroupSerivceImpl implements GroupService {

	@Autowired
	GroupDao groupDao;
	
	@Override
	public List<Group> getGroupList() {
		return groupDao.selectAll();
	}

	@Override
	public Group getGroup(int groupid) {
		return groupDao.selectOne(groupid);
	}

	@Override
	public void writeGroup(Group group) {
		groupDao.insertGroup(group);
	}

	@Override
	public void removeGroup(int groupid) {
		groupDao.deleteGroup(groupid);
	}

	@Override
	public void modifyGroup(Group group) {
		groupDao.updateGroup(group);
	}
	
}