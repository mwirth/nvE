package org.wimi.nve.example;

public class Person
{
	private String name;
	private String location;

	public Person(String name, String location)
	{
		this.name = name;
		this.location = location;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLocation()
	{
		return location;
	}

	public void setLocation(String location)
	{
		this.location = location;
	}

	public String toString()
	{
		return name + " " + location;
	}
}
