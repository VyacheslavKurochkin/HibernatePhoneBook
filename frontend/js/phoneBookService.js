import axios from "axios";

function executeGet(url, data) {
	return axios.get(url, {
		params: data
	}).then(response => response.data);
}

function executePost(url, data) {
	return axios.post(url, data).then(response => response.data);
}

function executeDelete(url, data) {
	return axios.delete(url, {data: data}).then(response => response.data);
}

export default class PhoneBookService {
	constructor() {
		this.baseUrl = "/api/contacts";
	}

	getContacts(term) {
		return executeGet(this.baseUrl, {term});
	}

	deleteContact(ids) {
		return executeDelete(this.baseUrl, ids);
	}

	saveContact(contact) {
		return executePost(this.baseUrl, contact);
	}
};