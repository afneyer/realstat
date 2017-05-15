package com.afn.realstat;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArtifactManager extends AbstractEntityManager<Artifact> {

	public static final Logger log = LoggerFactory.getLogger("import");

	@Autowired
	ArtifactRepository artcftRepository;
	
	public ArtifactManager(ArtifactRepository artfctRepository) {
		repo = artfctRepository;
	}

	@Transactional
	public void uploadIcons() {

		String iconDirectoryName = "C:\\afndev\\apps\\realstat\\resources\\VAADIN";
		// Resource resource = new ClassPathResource("icons");
		File iconDirectory = new File(iconDirectoryName);

		// get all the files from a directory
		if (iconDirectory != null) {
			File[] fList = iconDirectory.listFiles();
			if (fList != null) {
				for (File file : fList) {
					String fileName = file.getName();
					if ( fileName.matches("*.ico")) {
						Artifact artfct = new Artifact(file,"Icon");
						artfct.saveOrUpdate();
						System.out.println(file.getName());
					}
				}
			}
		}
		
		
	}

}
