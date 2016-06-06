package com.doto.tasktodo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.doto.tasktodo.message.Task;

@Repository
public class TaskDaoImpl implements TaskDao {

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public Task findById(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
		
        String sql = "SELECT * FROM tasks WHERE id=:id";				
        try {
        	Task result = namedParameterJdbcTemplate.queryForObject(sql, params, new TaskMapper());
            System.out.println("result => " + result);
            return result;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
        
	}

	public List<Task> findAll() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		String sql = "SELECT * FROM tasks";
		
        List<Task> result = namedParameterJdbcTemplate.query(sql, params, new TaskMapper());
        
        return result;
	}

	public int save(Task task) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("subject", task.getTaskSubject());
        params.put("description", task.getTaskDescription() == null && task.getTaskDescription() == "" ? "" : task.getTaskDescription());
        params.put("status", task.getTaskStatus());
        String sql = "INSERT INTO tasks (subject,description,status) VALUES (:subject,:description,:status);";
        int result = namedParameterJdbcTemplate.update(sql, params);
        return result;
	}

	public int update(String id, Task task) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
        params.put("subject", task.getTaskSubject());
        params.put("description", task.getTaskDescription() == null && task.getTaskDescription() == "" ? "" : task.getTaskDescription());
        params.put("status", task.getTaskStatus());
        String sql = "UPDATE tasks SET subject=:subject,description=:description,status=:status WHERE id=:id";
        int result = namedParameterJdbcTemplate.update(sql, params);	
        return result;
	}
	
	public int updateStatus(String id, String status) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        params.put("status", status);
        String sql = "UPDATE tasks SET status=:status WHERE id=:id";
        int result = namedParameterJdbcTemplate.update(sql, params);
        return result;
        
	}

	public int delete(String id) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
		String sql = "DELETE FROM tasks WHERE id=:id";
		int result = namedParameterJdbcTemplate.update(sql, params);
		return result;
	}
	
	private static final class TaskMapper implements RowMapper<Task> {

		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {			
			Task task = new Task();							
			task.setTaskId(rs.getInt("id"));			
			task.setTaskSubject(rs.getString("subject"));
			task.setTaskDescription(rs.getString("description"));
			task.setTaskStatus(rs.getString("status"));

			return task;
			
		}
	}
}
