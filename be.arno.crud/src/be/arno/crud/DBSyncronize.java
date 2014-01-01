package be.arno.crud;

import be.arno.crud.categories.CategoriesRepository;
import be.arno.crud.items.ItemsRepository;

public class DBSyncronize {
	
	
	public void toServer() {

		CategoriesRepository categoriesRepository = new CategoriesRepository(App.getContext());
		ItemsRepository 	 itemsRepository = new ItemsRepository(App.getContext());
		
		// itemsRepository.deleteAll();
		// categoriesRepository.deleteAll();

	}
	
	
}
