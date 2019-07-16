package com.dbs.loyalty.domain.file;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * Class of File Pdf Task
 * 
 * @author Ferry L. H. <ferrylinton@gmail.com>
 */
@Setter
@Getter
@Entity
@Table(name = "file_pdf_task")
public class FilePdfTask extends AbstractFile implements Serializable {

	private static final long serialVersionUID = 1L;

}
