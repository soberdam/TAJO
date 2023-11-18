package com.tajo.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tajo.dto.Record;
import com.tajo.dto.User;
import com.tajo.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api-user")
@Api(tags="유저 컨트롤러")
public class UserRestController {

	//UserService 라고 하는 친구를 주입
		@Autowired
		private UserService userService;
		
		@GetMapping
	    @ApiOperation(value="전체 유저 정보 반환")
	    public ResponseEntity<List<User>> getUserList() {
	        List<User> list = userService.getUserList();
	        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
	    }
		
		@GetMapping({"id"})
	    @ApiOperation(value="유저 정보 반환")
	    public ResponseEntity<User> getUserList(String id) {
	        User user = userService.getUser(id);
	        return new ResponseEntity<User>(user, HttpStatus.OK);
	    }
	        
			
		//회원가입을 해보자 form 태그 형식으로 넘어왔다.
		@PostMapping("signup")
		@ApiOperation(value="회원 가입", response = User.class)
		public ResponseEntity<Integer> signup(User user) {
			int result = userService.signup(user);
			
			//result 가 0이면 등록 x
			//result 가 1이면 등록 o
			return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
		}
		
		
		@PostMapping("login")
		@ApiOperation(value="로그인")
		public ResponseEntity<?> login(User user, HttpSession session) {
			User tmp = userService.login(user);
			//로그인 실패 (잘못했어)
			if(tmp == null)
				return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
			
			session.setAttribute("loginUser", tmp.getName());
			return new ResponseEntity<String>(tmp.getName(), HttpStatus.OK);
		}
		
		@GetMapping("logout")
		@ApiOperation(value="로그아웃")
		public ResponseEntity<Void> logout(HttpSession session) {
//			session.removeAttribute("loginUser");
			session.invalidate();
			
			return new ResponseEntity<Void>(HttpStatus.OK);
		}
		

		@GetMapping("record")
		@ApiOperation(value="라이딩 기록 불러오기")
		public ResponseEntity<Record> loadRecord(User user) {
			Record record = userService.getUserRecord(user);
			return new ResponseEntity<Record>(record, HttpStatus.OK);
		}
		
		@PostMapping("record")
		@ApiOperation(value="라이딩 기록 저장하기")
		public ResponseEntity<Integer> saveRecord(Record record) {
			int result = userService.setUserRecord(record);
			return new ResponseEntity<Integer>(result, HttpStatus.CREATED);
		}
		
		@GetMapping("average")
		@ApiOperation(value="유저 평균 기록과 비교한 주행 거리 불러오기")
		public ResponseEntity<Integer> loadAverage(User user) {
			Record record = userService.getUserRecord(user);
			int userDist = record.getDistance();
			int average = userService.getAverage();
			
			int result = userDist/average*100;
			
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}
		

}