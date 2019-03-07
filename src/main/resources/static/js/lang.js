var Lang = (function () {
	
	'use strict';

	var i18n = {
		"en": {
			"button": {
				"cancel": "Cancel",
				"close": "Close",
				"delete": "Delete",
				"upload": "Upload",
				"browse": "Browse"
			},
			"field": {
				"description": "Description",
				"email": "Email",
				"keyword": "Keyword",
				"name": "Name",
				"password": "Password",
				"username": "Username",
				"title": "Title",
				"file" : "File",
				"orderNumber": "Order Number"
			},
			"label":{
				"activated" : "Activated",
				"deactivated" : "Deactivated",
				"reject": "Reject",
				"verify": "Verify"
			},
			"message": {
				"DeleteConfirm": "Delete data ?",
				"ProcessSuccess": "The process was successful"
			},
			"error": {
				401: "Unauthorized",
				403: "Forbidden",
				404: "Not Found",
				405: "Method not allowed",
				406: "Not Acceptable",
				"ElementNotFound": "Element is not found",
				"ConstraintViolationException": "Data is referenced by other data",
				"ERR_CONNECTION_REFUSED": "Can not connect to server",
				"PickImage": "Pick image",
				"ImageName": "Name length min 3 chars and max 30 chars",
				"DuplicateEntry": "Duplicate data"
			}
		},
		"in": {
			"button": {
				"cancel": "Batal",
				"close": "Tutup",
				"delete": "Hapus",
				"upload": "Unggah",
				"browse": "Browse"
			},
			"field": {
				"description": "Keterangan",
				"email": "Email",
				"keyword": "Kata Kunci",
				"name": "Nama",
				"password": "Kata Sandi",
				"username": "Nama Pengguna",
				"title": "Judul",
				"file" : "Berkas",
				"orderNumber": "Nomor Urut"
			},
			"label":{
				"activated" : "Aktif",
				"deactivated" : "Tidak Aktif",
				"reject": "Ditolak",
				"verify": "Diverfikasi"
			},
			"message": {
				"DeleteConfirm": "Hapus data ?",
				"ProcessSuccess": "Proses berhasil"
			},
			"error": {
				401: "Unauthorized",
				403: "Forbidden",
				404: "Not Found",
				405: "Method not allowed",
				406: "Not Acceptable",
				"ElementNotFound": "tidak ditemukan",
				"ConstraintViolationException": "Data masih digunakan",
				"ERR_CONNECTION_REFUSED": "Tidak bisa melakukan koneksi",
				"PickImage": "Pilih gambar",
				"ImageName": "Nama min 3 karakter dan maks 30 karakter",
				"DuplicateEntry": "Data rangkap"
			}
		}
	}

	function button(code) {
		return lang('button', code);
	}

	function message(code) {
		return lang('message', code);
	}

	function field(code) {
		return lang('field', code);
	}
	
	function label(code) {
		return lang('label', code);
	}

	function error(code) {
		return lang('error', code);
	}

	function lang(type, code) {
		var locale = $('html').attr('lang');
		var result = '';

		if (locale == undefined) {
			result = i18n['in'][type][code];
		} else if(i18n[locale] != undefined){
			result = i18n[locale][type][code];
		}

		if (result == undefined || result == '') {
			return locale + '.' + type + '.' + code;
		} else {
			return result;
		}
	}

	return {
		button: button,
		message: message,
		field: field,
		label: label,
		error: error,
		lang: lang
	};

}());