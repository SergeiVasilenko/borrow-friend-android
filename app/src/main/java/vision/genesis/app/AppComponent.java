package vision.genesis.app;


import javax.inject.Singleton;

import dagger.Component;
import vision.genesis.manager.PermissionManager;
import vision.genesis.manager.PermissionModule;
import vision.genesis.network.Api;
import vision.genesis.network.NetworkModule;
import vision.genesis.network.request.util.DefaultErrorHandler;
import vision.genesis.storage.JacksonModule;
import vision.genesis.storage.Storage;
import vision.genesis.storage.StorageModule;
import vision.genesis.ui.UiComponent;
import vision.genesis.ui.UiModule;


@Singleton
@Component(modules = {NetworkModule.class,
					  AppModule.class,
					  UiModule.class,
					  PermissionModule.class,
					  JacksonModule.class,
					  StorageModule.class})
public interface AppComponent {

	UiComponent.UiComponentBuilder uiComponentBuilder();

	App application();

	ApplicationLifecycle applicationLifecycle();

	Api api();

	PermissionManager permissionManager();

	Storage storage();

	DefaultErrorHandler defaultErrorHandler();
}
