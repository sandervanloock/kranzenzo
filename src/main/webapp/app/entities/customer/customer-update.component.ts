import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';
import { ICustomer, Customer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';

@Component({
  selector: 'jhi-customer-update',
  templateUrl: './customer-update.component.html'
})
export class CustomerUpdateComponent implements OnInit {
  isSaving: boolean;

  addresses: ILocation[];

  editForm = this.fb.group({
    id: [],
    street: [],
    city: [],
    zipCode: [null, [Validators.min(1000), Validators.max(9999)]],
    province: [],
    phoneNumber: [],
    addressId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected customerService: CustomerService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ customer }) => {
      this.updateForm(customer);
    });
    this.locationService.query({ filter: 'customer-is-null' }).subscribe(
      (res: HttpResponse<ILocation[]>) => {
        if (!this.editForm.get('addressId').value) {
          this.addresses = res.body;
        } else {
          this.locationService
            .find(this.editForm.get('addressId').value)
            .subscribe(
              (subRes: HttpResponse<ILocation>) => (this.addresses = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  updateForm(customer: ICustomer) {
    this.editForm.patchValue({
      id: customer.id,
      street: customer.street,
      city: customer.city,
      zipCode: customer.zipCode,
      province: customer.province,
      phoneNumber: customer.phoneNumber,
      addressId: customer.addressId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const customer = this.createFromForm();
    if (customer.id !== undefined) {
      this.subscribeToSaveResponse(this.customerService.update(customer));
    } else {
      this.subscribeToSaveResponse(this.customerService.create(customer));
    }
  }

  private createFromForm(): ICustomer {
    return {
      ...new Customer(),
      id: this.editForm.get(['id']).value,
      street: this.editForm.get(['street']).value,
      city: this.editForm.get(['city']).value,
      zipCode: this.editForm.get(['zipCode']).value,
      province: this.editForm.get(['province']).value,
      phoneNumber: this.editForm.get(['phoneNumber']).value,
      addressId: this.editForm.get(['addressId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }
}
