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
				"activated": "Activated",
				"authorities": "Authorities",
				"description": "Description",
				"email": "Email",
				"keyword": "Keyword",
				"id": "Id",
				"imageBytes": "Image",
				"authenticateFromDb": "Authenticate From Db",
				"name": "Name",
				"password": "Password",
				"passwordHash": "Password",
				"role": "Role",
				"username": "Username",
				"title": "Title",
				"file" : "File",
				"orderNumber": "Order Number",
				"true": "Yes",
				"false": "No",
				"termAndCondition": "Term And Condition",
				"startPeriod": "Start Period",
				"endPeriod": "Endi Period",
				"showInCarousel": "Show In Carousel",
				"promoCategory": "Promo Category",
				"imageString": "Image",
				"code": "Code"
			},
			"label":{
				"activated" : "Activated",
				"deactivated" : "Deactivated",
				"no": "No",
				"reject": "Reject",
				"verify": "Verify",
				"yes": "Yes"
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
				"activated": "Aktif",
				"authorities": "Hak Akses",
				"description": "Keterangan",
				"email": "Email",
				"keyword": "Kata Kunci",
				"id": "Id",
				"imageBytes": "Gambar",
				"authenticateFromDb": "Login Dari Db",
				"name": "Nama",
				"password": "Kata Sandi",
				"passwordHash": "Kata Sandi",
				"role": "Peranan",
				"username": "Nama Pengguna",
				"title": "Judul",
				"file" : "Berkas",
				"orderNumber": "Nomor Urut",
				"true": "Ya",
				"false": "Tidak",
				"termAndCondition": "Syarat Dan Kondisi",
				"startPeriod": "Periode Mulai",
				"endPeriod": "Periode Akhir",
				"showInCarousel": "Tampilkan di Carousel",
				"promoCategory": "Kategori Promo",
				"imageString": "Gambar",
				"code": "Kode"
			},
			"label":{
				"activated" : "Aktif",
				"deactivated" : "Tidak Aktif",
				"no": "Tidak",
				"reject": "Ditolak",
				"verify": "Diverfikasi",
				"yes": "Ya"
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