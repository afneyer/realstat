package com.afn.realstat.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.vaadin.viritin.LazyList;
import org.vaadin.viritin.fields.LazyComboBox;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.fields.MValueChangeListener;

import com.afn.realstat.AbstractEntity;
import com.afn.realstat.AbstractEntityRepository;
import com.afn.realstat.Agent;
import com.afn.realstat.AgentRepository;
import com.afn.realstat.QAgent;
import com.querydsl.core.types.Predicate;
import com.vaadin.data.Property;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringComponent
public class Lov<T extends AbstractEntity> {

	public static String popUpWindow = "popUpWindow";
	public static String comboBox = "comboBox";

	AbstractEntityRepository<T> repo;

	private LazyComboBox<T> comboBoxLov;
	private Window popUpWindowLov;
	private T selected = null;
	private Class<T> type;

	public Lov() {
		setType((Class<T>) Agent.class);
		comboBoxLov = createLov();
	}

	public LazyComboBox<T> getComboBox() {
		return comboBoxLov;
	}

	public Window getPopUpWindowLov() {

		VerticalLayout popupContent = new VerticalLayout();

		popupContent.addComponent(comboBoxLov);
		// popupContent.addComponent(new Button("Button"));

		// The component itself
		popUpWindowLov = new Window("Agent Selector", popupContent);
		popUpWindowLov.setModal(true);
		popUpWindowLov.setContent(popupContent);
		return popUpWindowLov;

	}

	private LazyComboBox<T> createLov() {
		LazyComboBox<AbstractEntity> lov = new LazyComboBox<AbstractEntity>(AbstractEntity.class,

				new LazyComboBox.FilterablePagingProvider<AbstractEntity>() {

					@Override
					public List<AbstractEntity> findEntities(int firstRow, String filter) {
						// QAgent qa = QAgent.agent;
						// Predicate predicate = qa.agentName.containsIgnoreCase(filter);
						Predicate predicate = null; // TODO
						int batchSize = LazyList.DEFAULT_PAGE_SIZE;
						int pageNumber = firstRow / batchSize;
						PageRequest pr = new PageRequest(pageNumber, batchSize);
						Page<AbstractEntity> page = (Page<AbstractEntity>) repo.findAll(predicate, pr);
						List<AbstractEntity> list = page.getContent();
						return list;
					}
				}, new LazyComboBox.FilterableCountProvider() {

					@Override
					public int size(String filter) {
						// QAgent qa = QAgent.agent;
						// Predicate predicate = qa.agentName.startsWith(filter);
						Predicate predicate = null;

						long count = repo.count(predicate);
						return (int) count;
					}

				});

		// TODO figure out what to do with this one
		@SuppressWarnings("serial")
		MValueChangeListener<T> listener = new MValueChangeListener<T>() {
			@Override
			public void valueChange(MValueChangeEvent<T> event) {
				// TOOD figure out why case it necessary
				Property<T> p = event.getProperty();
				T entity = p.getValue();
				setSelected(entity);
			}
		};
		lov.addMValueChangeListener((MValueChangeListener<AbstractEntity>) listener);
		return (LazyComboBox<T>) lov;

	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public T getSelected() {
		return selected;
	}

	public void setSelected(T selected) {
		this.selected = selected;
	}

}
