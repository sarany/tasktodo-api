package com.doto.tasktodo.dao;

import java.util.List;

import com.doto.tasktodo.message.Task;

public interface TaskDao {

	Task findById(String id);
	List<Task> findAll();
	int save(Task task);
	int update(String id, Task task);
	int updateStatus(String id, String status);
	int delete(String id);
	
}