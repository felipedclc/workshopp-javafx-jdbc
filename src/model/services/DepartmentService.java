package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();

	public List<Department> findAll(){
		return dao.findAll(); // BUSCA TODOS OS DEPARTAMENTOS NO BANCO DE DADOS
	}
	
	public void saveOrUpdate(Department obj) { // METODO PARA INSERIR OU SALVAR O DEPARTMANETO NO BANCO 
		if(obj.getId() == null) {
			dao.insert(obj);
		}
		else {
			dao.update(obj);
		}
	}
}
