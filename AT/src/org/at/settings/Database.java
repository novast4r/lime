package org.at.settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	private Connection connection = null;

	public boolean connect(){
		java.io.File f = new java.io.File("settings.db");
		boolean create = !f.exists();
			
		 try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:settings.db");  
			connection.setAutoCommit(true);
			
			if(create) {
				populate();
				//this.insertController("192.168.1.70", "8080");
				//this.insertHost("agostino", "192.168.1.67", null);
				//this.insertHost("stefano", "192.168.1.70", null);
			}
			
			if (connection != null)
				return true;
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void disconnect() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean populate(){
		try {
			final Statement s1 = connection.createStatement();
			s1.execute(
					"create table host(nome varchar not null," +
					"ip varchar , port varchar, primary key (ip) );");
			final Statement s2 = connection.createStatement();
			s2.execute("create table controller(ip varchar, port varchar);");
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/*******************INSERT***********************************/
	public boolean insertHost(String nome, String ip, String port){
		try {
			final Statement s = connection.createStatement();
			boolean esito = s.execute("insert into host values(\""+ 
					nome + "\" ,\"" + ip +"\" , \"" + port +"\");"); 
			return esito;
		} catch (SQLException e) {e.printStackTrace();}
		return false;
	}
	
	public boolean insertController(String ip, String port){
		try {
			final Statement s = connection.createStatement();
			boolean esito = s.execute("insert into controller values(\"" +
					ip +"\" , \"" + port +"\");");
			return esito;
		} catch (SQLException e) {e.printStackTrace();}
		return false;
	}
	
	/*********************SELECT***********************/
	public ResultSet selectHost(String nome){
		try {
			final Statement s = connection.createStatement();
			final ResultSet resultSet = s.executeQuery(
					"select * from host where nome=\""+nome+ "\";");
			return resultSet;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
	public ResultSet selectAllHost(){
		try {
			final Statement s = connection.createStatement();
			final ResultSet resultSet = s.executeQuery(
					"select nome,ip,port from host;");
			return resultSet;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
	public ResultSet selectController(){
		try {
			final Statement s = connection.createStatement();
			final ResultSet resultSet = s.executeQuery(
					"select ip, port from controller;");
			return resultSet;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	
	/*********************DELETE & DROP***********************/
	public boolean deleteHost(String ip){
		try {
			final Statement s = connection.createStatement();
			boolean esito = s.execute(
					"delete from host where ip = \""+ip+"\";");
			return esito;
		} catch (SQLException e) {e.printStackTrace();}
		return false;
	}
	
	public boolean deleteController(String ip){
		try {
			final Statement s = connection.createStatement();
			boolean esito = s.execute(
					"delete from controller where ip = \" "+ip+"\";");
			return esito;
		} catch (SQLException e) {e.printStackTrace();}
		return false;
	}
	
	public boolean deleteController(){
		try {
			final Statement s = connection.createStatement();
			boolean esito = s.execute(
					"delete from controller ;");
			return esito;
		} catch (SQLException e) {e.printStackTrace();}
		return false;
	}

}
