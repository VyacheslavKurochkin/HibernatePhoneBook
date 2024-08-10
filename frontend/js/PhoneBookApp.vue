<template>
	<div class="container my-3">
		<h1 class="mb-3">Телефонная книга</h1>
		<form @submit.prevent="createContact" class="row mb-3">
			<h2 class="h5">Добавить контакт</h2>
			<div class="row gy-2">
				<div class="col-md-5">
					<input v-model.trim="name"
						   @keydown="isInvalidName = false"
						   :class="{'is-invalid': isInvalidName}"
						   type="text"
						   class="form-control"
						   placeholder="Имя">
					<div id="validation-edit-name" class="invalid-feedback">
						Поле обязательно для заполнения
					</div>
				</div>
				<div class="col-md-3">
					<input v-model.trim="phone"
						   @keydown="isInvalidPhone = false"
						   :class="{'is-invalid': isInvalidPhone}"
						   type="text"
						   class="form-control"
						   placeholder="Телефон">
					<div id="validation-edit-phone" class="invalid-feedback">
						Поле обязательно для заполнения
					</div>
				</div>
				<div class="col-2">
					<button class="btn btn-primary">Добавить</button>
				</div>
			</div>
		</form>
		<form @submit.prevent="loadContacts" class="mb-3">
			<h2 class="h5">Поиск контактов</h2>
			<div class="row row-cols-lg-auto g-2 align-items-center">
				<div class="col-12">
					<input v-model.trim="term" type="text" class="form-control" placeholder="Текст поиска">
				</div>
				<div class="col-12">
					<button class="btn btn-primary">Поиск</button>
				</div>
			</div>
		</form>

		<div class="">
			<button @click="showDeleteContactConfirm(null)"
					class="btn btn-danger"
					type="button">
				Удалить выбранные
			</button>
		</div>

		<div class="table-responsive-lg" v-cloak>
			<table class="table table-striped text-center">
				<thead>
				<tr class="align-middle">
					<th class="col-1">
						<input
							:checked="contacts.length > 0 && selectedContactsIndexes.length === contacts.length"
							@click="selectAllContacts"
							class="form-check-input"
							type="checkbox">
					</th>
					<th class="col-1">№</th>
					<th class="col-4">Имя</th>
					<th class="col-3">Телефон</th>
					<th></th>
				</tr>
				</thead>
				<tbody class="align-middle">
				<tr v-for="(contact, index) in contacts" :key="contact.id" class="p-3">
					<td>
						<input :checked="selectedContactsIndexes.includes(index)"
							   @click="selectContact(index)"
							   class="form-check-input"
							   type="checkbox">
					</td>
					<td v-text="index + 1"></td>
					<template v-if="!editingContacts.has(contact.id)">
						<td v-text="contact.name"></td>
						<td v-text="contact.phone"></td>
						<td>
							<div class="row justify-content-end">
								<div class="col g-2 g-lg-0">
									<button @click="initEditingContact(index)"
											class="btn btn-primary me-2"
											type="button">
										Редактировать
									</button>
								</div>
								<div class="col g-2 g-lg-0">
									<button @click="showDeleteContactConfirm(index)"
											class="btn btn-danger"
											type="button">
										Удалить
									</button>
								</div>
							</div>
						</td>
					</template>
					<template v-else>
						<td>
							<div class="input-group has-validation">
								<input @keydown="contact.isInvalidEditName = false"
									   @keyup.enter="saveContact(index)"
									   v-model.trim="contact.editingName"
									   :class="{'is-invalid': contact.isInvalidEditName}"
									   class="form-control"
									   type="text">
							</div>
						</td>
						<td>
							<div class="input-group has-validation">
								<input @keydown="contact.isInvalidEditPhone = false"
									   @keyup.enter="saveContact(index)"
									   v-model.trim="contact.editingPhone"
									   :class="{'is-invalid': contact.isInvalidEditPhone}"
									   class="form-control"
									   type="text">
							</div>
						</td>
						<td>
							<div class="row justify-content-end">
								<div class="col g-2 g-lg-0">
									<button @click="saveContact(index)"
											class="btn btn-primary me-2"
											type="button">
										Сохранить
									</button>
								</div>
								<div class="col g-2 g-lg-0">
									<button @click="cancelEditingContact(index)"
											class="btn btn-danger"
											type="button">
										Отменить
									</button>
								</div>
							</div>
						</td>
					</template>
				</tr>
				</tbody>
			</table>
		</div>
	</div>

	<div ref="errorModalDialog" class="modal fade" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Действие не выполнено</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<span v-text="errorDescription"></span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
						Закрыть
					</button>
				</div>
			</div>
		</div>
	</div>

	<div ref="deleteConfirmModalDialog" class="modal fade" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Подтверждение удаления</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<span>Удалить контакт?</span>
				</div>
				<div class="modal-footer">
					<button @click="cancelDeleteContact" type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">
						Отмена
					</button>
					<button @click="deleteContacts" type="button" class="btn btn-danger">
						Удалить
					</button>
				</div>
			</div>
		</div>
	</div>

	<div ref="deleteSelectedContactsConfirmModalDialog" class="modal fade" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Подтверждение удаления</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<span>Удалить выбранные контакты?</span>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
						Отмена
					</button>
					<button @click="deleteContacts" type="button" class="btn btn-danger">
						Удалить
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
import * as bootstrap from "bootstrap/dist/js/bootstrap.bundle";
import PhoneBookService from "./phoneBookService";
//import * as constants from "node:constants";

export default {
	name: "app",

	data() {
		return {
			contacts: [],
			editingContacts: new Map(),
			service: new PhoneBookService(),
			selectedContactsIndexes: [],

			term: "",
			name: "",
			phone: "",

			isInvalidName: false,
			isInvalidPhone: false,
			selectedContactIndex: -1,
			errorDescription: "",
			deleteConfirmDialog: null,
			deleteSelectedContactsConfirmDialog: null,
			errorDialog: null
		};
	},

	created() {
		this.loadContacts();
	},

	mounted() {
		this.deleteConfirmDialog = new bootstrap.Modal(this.$refs.deleteConfirmModalDialog, {});
		this.deleteSelectedContactsConfirmDialog = new bootstrap.Modal(this.$refs.deleteSelectedContactsConfirmModalDialog, {});
		this.errorDialog = new bootstrap.Modal(this.$refs.errorModalDialog, {});
	},

	methods: {
		createContact() {
			if (this.name.length === 0) {
				this.isInvalidName = true;
			}

			if (this.phone.length === 0) {
				this.isInvalidPhone = true;
			}

			if (this.isInvalidName || this.isInvalidPhone) {
				return;
			}

			const contact = {
				name: this.name,
				phone: this.phone
			};

			this.service.saveContact(contact).then(response => {
				if (!response.success) {
					this.showErrorDialog(response.message);

					return;
				}

				this.name = "";
				this.phone = "";

				this.loadContacts();
			}).catch(() => this.showErrorDialog("Новый контакт не добавлен"));
		},

		saveContact(contactIndex) {
			const editingContact = this.contacts[contactIndex];

			if (editingContact.editingName.length === 0) {
				this.showErrorDialog("Поле 'Имя' обязательно для заполнения!");

				editingContact.isInvalidEditName = true;
			}

			if (editingContact.editingPhone.length === 0) {
				this.showErrorDialog("Поле 'Телефон' обязательно для заполнения!");

				editingContact.isInvalidEditPhone = true;
			}

			if (editingContact.isInvalidEditPhone || editingContact.isInvalidEditName) {
				return;
			}

			const contact = {
				id: editingContact.id,
				name: editingContact.editingName,
				phone: editingContact.editingPhone
			};

			this.service.saveContact(contact).then(response => {
				if (!response.success) {
					this.showErrorDialog(response.message);

					return;
				}

				this.contacts[contactIndex].name = editingContact.editingName;
				this.contacts[contactIndex].phone = editingContact.editingPhone;

				this.editingContacts.delete(this.contacts[contactIndex].id);
			}).catch(() => this.showErrorDialog("Контакт не изменен"))
		},

		showDeleteContactConfirm(contactIndex) {
			if (contactIndex !== null) {
				this.selectedContactIndex = contactIndex;
				this.deleteConfirmDialog.show();
			} else {
				if (this.selectedContactsIndexes.length === 0) {
					return;
				}

				this.deleteSelectedContactsConfirmDialog.show();
			}
		},

		cancelDeleteContact() {
			this.selectedContactIndex = -1;
		},

		showErrorDialog(errorText) {
			this.errorDescription = errorText;
			this.errorDialog.show();
		},

		selectContact(contactIndex) {
			if (this.selectedContactsIndexes.includes(contactIndex)) {
				this.selectedContactsIndexes = this.selectedContactsIndexes.filter(index => index !== contactIndex)

				return;
			}

			this.selectedContactsIndexes.push(contactIndex);
		},

		selectAllContacts() {
			if (!this.contacts.length) {
				return;
			}

			if (this.selectedContactsIndexes.length === this.contacts.length) {
				this.selectedContactsIndexes = [];

				return;
			}

			this.selectedContactsIndexes = [];

			this.contacts.forEach((contact, index) => {
				this.selectedContactsIndexes.push(index);
			});
		},

		addEditingContact(contact) {
			const editingContact = {
				id: contact.id,
				editingName: contact.editingName,
				editingPhone: contact.editingPhone
			}

			this.editingContacts.set(contact.id, editingContact);
		},

		initEditingContact(contactIndex) {
			const editingContact = this.contacts[contactIndex];
			editingContact.isInvalidEditName = false;
			editingContact.isInvalidEditPhone = false;
			editingContact.editingName = editingContact.name;
			editingContact.editingPhone = editingContact.phone;

			this.addEditingContact(editingContact);
		},

		cancelEditingContact(contactIndex) {
			this.contacts[contactIndex].isInvalidEditName = false;
			this.contacts[contactIndex].isInvalidEditPhone = false;

			this.editingContacts.delete(this.contacts[contactIndex].id);
		},

		async deleteContacts() {
			const deletingContactsIds = [];

			if (this.selectedContactIndex > -1) {
				this.deleteConfirmDialog.hide();

				deletingContactsIds.push(this.contacts[this.selectedContactIndex].id);
			} else {
				this.deleteSelectedContactsConfirmDialog.hide();

				this.selectedContactsIndexes.forEach((index) => {
					deletingContactsIds.push(this.contacts[index].id);
				});
			}

			await this.service.deleteContact(deletingContactsIds).then(response => {
				if (!response.success) {
					this.showErrorDialog(response.message);
				}

				this.selectedContactIndex = -1;
				this.selectedContactsIndexes = [];
			}).catch(() => this.showErrorDialog("Контакт не удален"));

			this.loadContacts();
		},

		async deleteContact(contact) {
			await this.service.deleteContact(contact.id).then(response => {
				if (!response.success) {
					this.showErrorDialog(response.message);
				}
			}).catch(() => this.showErrorDialog("Контакт не удален"));
		},

		saveEditingContacts() {
			this.contacts.forEach(contact => {
				const editingContact = this.editingContacts.get(contact.id);

				if (editingContact) {
					editingContact.editingName = contact.editingName;
					editingContact.editingPhone = contact.editingPhone;
				}
			});
		},

		updateLoadedContacts() {
			this.contacts.forEach(contact => {
				contact.isInvalidEditName = false;
				contact.isInvalidEditPhone = false;

				const editingContact = this.editingContacts.get(contact.id);

				if (editingContact) {
					contact.isEditing = true;
					contact.editingName = editingContact.editingName;
					contact.editingPhone = editingContact.editingPhone;
				} else {
					contact.isEditing = false;
					contact.editingName = contact.name;
					contact.editingPhone = contact.phone;
				}
			});
		},

		loadContacts() {
			this.saveEditingContacts();

			this.service.getContacts(this.term).then(contacts => {
				this.contacts = contacts;

				this.updateLoadedContacts();
			}).catch(() => this.showErrorDialog("Список контактов не загружен"));
		}
	}
};
</script>