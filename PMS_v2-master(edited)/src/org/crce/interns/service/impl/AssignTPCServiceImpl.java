package org.crce.interns.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.crce.interns.beans.FacultyUserBean;
import org.crce.interns.beans.UserDetailsBean;
import org.crce.interns.dao.AssignTPCDao;
import org.crce.interns.model.FacultyUser;
import org.crce.interns.model.RMUser;
import org.crce.interns.model.UserDetails;
import org.crce.interns.service.AssignTPCService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignTPCServiceImpl implements AssignTPCService {
	@Autowired
	private AssignTPCDao assignTPCDao;

	/* Methods to Insert the data */
	@Override
	public int assignTPC(UserDetailsBean userBean) {
		UserDetails user = new UserDetails();
		UserDetails checkUser = new UserDetails();
		RMUser rmuser = new RMUser();

		String st;
		BeanUtils.copyProperties(userBean, user);
		checkUser.setUserName(userBean.getUserName());

		checkUser = assignTPCDao.getUser(checkUser);

		System.out.println("User Role ID from JSP : " + userBean.getRoleId() + "\n");
		String roleID = userBean.getRoleId();
		System.out.println(roleID);
		rmuser = assignTPCDao.getUserRole(roleID);

		System.out.println("User Id in RM Table: " + rmuser.getRole_id());
		System.out.println("User Role in RM Table: " + rmuser.getUserRole());

		if (checkUser == null) {
			System.out.println("Error:No User Defined" + "\n");
			return 0;
		}

		System.out.println("User Role ID from DB : " + checkUser.getRoleId() + "\n");
		/*
		 * 1-Student 2-Faculty 3-Student-TPC 4-Faculty-TPC
		 */
		st = userBean.getRoleId();
		if (checkUser.getRoleId().equalsIgnoreCase("3")||checkUser.getRoleId().equalsIgnoreCase("4")) {
			return 34;
		}
		else if (st.equalsIgnoreCase("1")) {

			if (checkUser.getRoleId().equalsIgnoreCase(userBean.getRoleId())) {
				System.out.println("Before update Student Role ID : " + checkUser.getRoleId() + "\n");
				checkUser.setRoleId("3");
				System.out.println("After update Student Role ID : " + checkUser.getRoleId() + "\n");
				assignTPCDao.assignTPC(checkUser);
				return 1;
			} else {
				System.out.println("Invalid Input: Student" + "\n");
				return 3;
			}
		} else if (st.equalsIgnoreCase("2")) {
			System.out.println(userBean.getRoleId());
			if (checkUser.getRoleId().equalsIgnoreCase(userBean.getRoleId())) {
				System.out.println("Before update Faculty Role ID : " + checkUser.getRoleId() + "\n");
				checkUser.setRoleId("4");
				System.out.println("After update Faculty Role ID : " + checkUser.getRoleId() + "\n");
				assignTPCDao.assignTPC(checkUser);
				return 1;
			} else {
				System.out.println("Invalid Input : Faculty" + "\n");
				return 4;
			}
		} else {
			System.out.println("Error : No Such User Exists");
			return 0;
		}

	}

	@Override
	public int insertWork(FacultyUserBean fuserBean) {
		FacultyUser fuser = new FacultyUser();

		fuser.setUserName(fuserBean.getUserName());

		fuser = assignTPCDao.getFacultyUser(fuser);
		System.out.println(fuser);
		if (fuser == null) {
			System.out.println("Error: No such User Defined" + "\n");
			return 0;
		}
		
		System.out.println("UserWorkk in Service with Bean: " + fuserBean.getUserWork());
		fuser.setUserWork(fuserBean.getUserWork());
		System.out.println("Username in Service IMPL :" + fuser.getUserName());
		System.out.println("UserWork in Service IMPL :" + fuser.getUserWork());

		assignTPCDao.insertWork(fuser);
		return 1;
		
	}

	public List<UserDetailsBean> convertToBean(List<UserDetails> userList) {
		List<UserDetailsBean> userBeanList = new ArrayList<UserDetailsBean>();
		for (UserDetails user : userList) {
			UserDetailsBean userBean = new UserDetailsBean();
			BeanUtils.copyProperties(user, userBean);
			userBeanList.add(userBean);
		}
		return userBeanList;
	}

	public List<FacultyUserBean> convertToBeanFaculty(List<FacultyUser> userList) {
		List<FacultyUserBean> userBeanList = new ArrayList<FacultyUserBean>();
		for (FacultyUser fuser : userList) {
			FacultyUserBean fuserBean = new FacultyUserBean();
			BeanUtils.copyProperties(fuser, fuserBean);
			userBeanList.add(fuserBean);
		}
		return userBeanList;
	}

	@Override
	public int removeTPC(UserDetailsBean userBean) {
		// TODO Auto-generated method stub
		UserDetails user = new UserDetails();
		UserDetails checkUser = new UserDetails();
		BeanUtils.copyProperties(userBean, user);
		checkUser.setUserName(userBean.getUserName());

		checkUser = assignTPCDao.getUser(checkUser);

		if (checkUser == null) {
			System.out.println("Error: No User Defined" + "\n");
			return 0;
		}

		if (checkUser.getRoleId().equalsIgnoreCase("3")) {
			System.out.println("Before update Student Role : " + checkUser.getRoleId() + "\n");
			checkUser.setRoleId("1");// 1 is Student & 3 is Student tpc
			System.out.println("After update Student Role : " + checkUser.getRoleId() + "\n");
			assignTPCDao.removeTPC(checkUser);
			return 1;
		} else if (checkUser.getRoleId().equalsIgnoreCase("4")) {
			System.out.println("Before update Faculty Role : " + checkUser.getRoleId() + "\n");
			checkUser.setRoleId("2");// 2 is faculty & 4 is Fac tpc
			System.out.println("After update Faculty Role : " + checkUser.getRoleId() + "\n");
			assignTPCDao.removeTPC(checkUser);
			return 1;
		}
		else {
			System.out.println("Service :Error (LE) : No Such User Exists Last Error");
			return 33;
		}
	}

	@Override
	public List<UserDetailsBean> viewUsers() {
		// TODO Auto-generated method stub
		List<UserDetails> userList = assignTPCDao.viewUsers();
		return convertToBean(userList);
	}

	@Override
	public List<FacultyUserBean> viewFacultyTasks() {
		// TODO Auto-generated method stub
		List<FacultyUser> userList = assignTPCDao.viewFacultyTasks();
		return convertToBeanFaculty(userList);
	}

}