package vision.genesis.storage;

import android.content.Context;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import vision.genesis.app.App;
import vision.genesis.model.Session;
import vision.genesis.model.LocalUser;
import vision.genesis.util.log.LogManager;

public class JacksonStorage {

	private static final String     TAG = "JacksonStorage";
	private static final LogManager log = new LogManager(TAG);

	private static final String SESSION = "session.obj";
	private static final String USER    = "user.obj";

	private final ObjectMapper mMapper;

	private App mApp;

	@Inject
	public JacksonStorage(App app, ObjectMapper objectMapper) {
		mApp = app;
		mMapper = objectMapper;
	}

	@Nullable
	private <E> E read(String name, TypeReference<E> typeReference) {
		E object = null;
		String text;
		try {
			FileInputStream fis = mApp.openFileInput(name);
			BufferedInputStream bis = new BufferedInputStream(fis);
			object = mMapper.readValue(bis, typeReference);
			fis.close();
		} catch (FileNotFoundException e) {
			log.w("FileNotFound");
			// no file, do nothing
		} catch (JsonParseException e) {
			log.w("parse error", e);
		} catch (JsonMappingException e) {
			log.w("parse error", e);
			try {
				FileInputStream fis = mApp.openFileInput(name);
				BufferedInputStream bis = new BufferedInputStream(fis);
				text = convertStreamToString(bis);
				log.w("parse error: text to parsing: %s", text);
			} catch (FileNotFoundException e1) {
				//do nothing
			}
		} catch (EOFException e) {
			log.w("eof: ", e);
			try {
				FileInputStream fis = mApp.openFileInput(name);
				BufferedInputStream bis = new BufferedInputStream(fis);
				text = convertStreamToString(bis);
				log.w("eof: text to parsing: %s", text);
			} catch (FileNotFoundException e1) {
				//do nothing
			}
		} catch (IOException e) {
			log.w("parse error", e);
		}
		return object;
	}

	public void clearStorage() {
		mApp.deleteFile(SESSION);
		mApp.deleteFile(USER);
		log.d("JacksonStorage is cleared");
	}

	private void write(String name, Object value) {
		try {
			log.d("try to write " + name);
			FileOutputStream fos = mApp.openFileOutput(name, Context.MODE_PRIVATE);
			mMapper.writeValue(fos, value);
			fos.close();
		} catch (IOException e) {
			log.w("", e);
		}
	}

	public void setSession(@Nullable Session session) {
		if (session == null) {
			log.w("setSession: delete session");
			mApp.deleteFile(SESSION);
		} else {
			if (session.getToken() == null) {
				log.w("setSession: token is null");
			}
			write(SESSION, session);
		}
	}

	@Nullable
	public Session getSession() {
		return read(SESSION, new TypeReference<Session>() {
		});
	}

	public void setCurrentUser(@Nullable LocalUser currentUser) {
		if (currentUser == null) {
			log.w("setCurrentUser: delete user");
			mApp.deleteFile(USER);
		} else {
			write(USER, currentUser);
		}
	}

	@Nullable
	public LocalUser getCurrentUser() {
		return read(USER, new TypeReference<LocalUser>() {
		});
	}

	private static String convertStreamToString(InputStream is) {
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}
}