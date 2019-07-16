package com.dbs.loyalty.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class of Image Task
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Entity
@Table(name = "file_image_task")
public class FileImageTask extends AbstractFileImage implements Serializable {

	private static final long serialVersionUID = 1L;
	
}
