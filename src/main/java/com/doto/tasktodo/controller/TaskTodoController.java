package com.doto.tasktodo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.doto.tasktodo.dao.TaskDao;
import com.doto.tasktodo.message.Task;
import com.doto.tasktodo.message.TaskListResponse;

/**
 * @author Saranya Sae.
 * Created on 05/06/2016
 * 
 */

@Controller
@RequestMapping("/v1")
public class TaskTodoController {

	@Autowired
	TaskDao taskDao;
	
	// get all list of task
	@RequestMapping(value="/tasks", method=RequestMethod.GET)
	public @ResponseBody TaskListResponse retrieveTaskList() {
		TaskListResponse response = new TaskListResponse();
		List<Task> tasksList = taskDao.findAll();
		response.setTasksList(tasksList);
		return response;
	}
	
	// get task by id
	@RequestMapping(value="/task/id/{id}", method=RequestMethod.GET)
	public @ResponseBody Task retrieveTask(@PathVariable("id") String id) {								
		// query task from database
		Task task = taskDao.findById(id);
		
		if(task != null){	
			return task;
		}else{
			Task taskNotFound = new Task();
			taskNotFound.setTaskId(Integer.parseInt(id));
			taskNotFound.setTaskStatus("Data Not Found");			
			
			return taskNotFound;				
		}								
	}
	
	// create new task
	@RequestMapping(value="/task", method=RequestMethod.POST, produces="application/json", consumes="application/json")
	public ResponseEntity<Object> createTask(@RequestBody Task request) {
		int result = 0;
		// check not null request parameter (subject and status)
		if((request.getTaskSubject() != null && request.getTaskSubject() != "") && request.getTaskStatus() != null && request.getTaskStatus() != ""){	
			result = taskDao.save(request);
		}
		// check insert task complete
		if(result > 0){
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	// update task by id
	@RequestMapping(value="/task/id/{id}", method=RequestMethod.PUT, produces="application/json", consumes="application/json")
	public ResponseEntity<Object> updateTask(@PathVariable("id") String id, @RequestBody Task request) {		
		int result = 0;
		// check not null request parameter (subject and status)
		if((request.getTaskSubject() != null && request.getTaskSubject() != "") && request.getTaskStatus() != null && request.getTaskStatus() != ""){
			result = taskDao.update(id, request);			
		}
		System.out.println("result create task => " + result);
		// check update task is complete
		if(result>0){
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	// edit status of task by id
	@RequestMapping(value="/task/id/{id}/status/{status}", method=RequestMethod.PATCH)
	public ResponseEntity<Object> updateTaskStatus(@PathVariable("id") String id, @PathVariable("status") String status) {
		int resutl = 0;
		// check status and id is not null
		if((id != null && id != "") && (status != null && status != "")){
			resutl = taskDao.updateStatus(id, status);
		}
		// check update status is complete
		if(resutl>0){
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	
	// delete task by id
	@RequestMapping(value="/task/id/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Object> removeTask(@PathVariable("id") String id) {
		int result = 0;
		// check id is not null
		if(id != null && id != ""){
			result = taskDao.delete(id);
		}
		// check delete task is complete
		if(result>0){
			return new ResponseEntity<Object>(HttpStatus.OK);
		}		
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
