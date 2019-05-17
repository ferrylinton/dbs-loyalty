var Lang = (function () {
	
	'use strict';

	var i18n = {
		"en": {
			"field": {
				"activated": "Activated",
				"authorities": "Authorities",
				"content": "Content",
				"description": "Description",
				"email": "Email",
				"keyword": "Keyword",
				"id": "Id",
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
				"endPeriod": "End Period",
				"showInCarousel": "Show In Carousel",
				"promoCategory": "Promo Category",
				"code": "Code",
				"locked": "Locked",
				"userType": "Type",
				"phone": "Phone",
				"customerType": "Type",
				"dob": "Date Of Birth",
				"image": "Image",
				"material": "Material",
				"place": "Place",
				"timePeriod": "Time",
				"feedback": "Feedback"
			},
			"label":{
				"activated" : "Activated",
				"deactivated" : "Deactivated",
				"locked" : "Locked",
				"unlocked" : "Unlocked",
				"showInCarousel": "Show In Carousel",
				"notShowInCarousel": "Not Show In Carousel",
				"viewImage" : "View Image",
				"viewPdf" : "View Pdf"
			}
		},
		"id": {
			"field": {
				"activated": "Aktif",
				"authorities": "Hak Akses",
				"content": "Konten",
				"description": "Keterangan",
				"email": "Email",
				"keyword": "Kata Kunci",
				"id": "Id",
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
				"code": "Kode",
				"locked": "Dikunci",
				"userType": "Tipe",
				"phone": "Telepon",
				"customerType": "Tipe",
				"dob": "Tanggal Lahir",
				"image": "Gambar",
				"material": "Materi",
				"place": "Tempat",
				"timePeriod": "Jam",
				"feedback": "Feedback"
			},
			"label":{
				"activated" : "Aktif",
				"deactivated" : "Tdk Aktif",
				"locked" : "Terkunci",
				"unlocked" : "Tdk Terkunci",
				"showInCarousel": "Tampil Di Carousel",
				"notShowInCarousel": "Tdk Tampil Di Carousel",
				"viewImage" : "Lihat Gambar",
				"viewPdf" : "Lihat Pdf"
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

	function getLocale(){
		var locale = $('html').attr('lang');
		
		if (locale == undefined) {
			locale = 'id';
		}
		
		return locale;
	}

	return {
		button: button,
		message: message,
		field: field,
		label: label,
		error: error,
		lang: lang,
		getLocale: getLocale
	};

}());