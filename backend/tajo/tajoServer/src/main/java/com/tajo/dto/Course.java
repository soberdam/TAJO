package com.tajo.dto;

public class Course {
	String name;
	String content;
	String location;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Course [name=" + name + ", content=" + content + "]";
	}

}