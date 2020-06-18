package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao dao = DaoFactory.createSellerDao();

	public List<Seller> findAll() {
		return dao.findAll(); // BUSCA TODOS OS VENDEDORES NO BANCO DE DADOS
	}

	public void saveOrUpdate(Seller obj) { // METODO PARA INSERIR OU SALVAR O VENDEDORES NO BANCO
		if (obj.getId() == null) {
			dao.insert(obj);
		} else {
			dao.update(obj);
		}
	}

	public void remove(Seller obj) { // REMOVENDO UM VENDEDOR DO BANCO DE DADOS
		dao.deleteById(obj.getId());
	}
}
