import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IProductOrder, ProductOrder } from 'app/shared/model/product-order.model';
import { ProductOrderService } from './product-order.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer/customer.service';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location/location.service';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product/product.service';

@Component({
  selector: 'jhi-product-order-update',
  templateUrl: './product-order-update.component.html'
})
export class ProductOrderUpdateComponent implements OnInit {
  isSaving: boolean;

  customers: ICustomer[];

  deliveryaddresses: ILocation[];

  products: IProduct[];

  editForm = this.fb.group({
    id: [],
    created: [],
    updated: [],
    state: [],
    deliveryType: [],
    includeBatteries: [],
    description: [null, [Validators.maxLength(5000)]],
    deliveryPrice: [null, [Validators.min(0)]],
    paymentType: [null, [Validators.required]],
    customerId: [],
    deliveryAddressId: [],
    productId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected productOrderService: ProductOrderService,
    protected customerService: CustomerService,
    protected locationService: LocationService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ productOrder }) => {
      this.updateForm(productOrder);
    });
    this.customerService
      .query()
      .subscribe((res: HttpResponse<ICustomer[]>) => (this.customers = res.body), (res: HttpErrorResponse) => this.onError(res.message));
    this.locationService.query({ filter: 'productorder-is-null' }).subscribe(
      (res: HttpResponse<ILocation[]>) => {
        if (!this.editForm.get('deliveryAddressId').value) {
          this.deliveryaddresses = res.body;
        } else {
          this.locationService
            .find(this.editForm.get('deliveryAddressId').value)
            .subscribe(
              (subRes: HttpResponse<ILocation>) => (this.deliveryaddresses = [subRes.body].concat(res.body)),
              (subRes: HttpErrorResponse) => this.onError(subRes.message)
            );
        }
      },
      (res: HttpErrorResponse) => this.onError(res.message)
    );
    this.productService
      .query()
      .subscribe((res: HttpResponse<IProduct[]>) => (this.products = res.body), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(productOrder: IProductOrder) {
    this.editForm.patchValue({
      id: productOrder.id,
      created: productOrder.created != null ? productOrder.created.format(DATE_TIME_FORMAT) : null,
      updated: productOrder.updated != null ? productOrder.updated.format(DATE_TIME_FORMAT) : null,
      state: productOrder.state,
      deliveryType: productOrder.deliveryType,
      includeBatteries: productOrder.includeBatteries,
      description: productOrder.description,
      deliveryPrice: productOrder.deliveryPrice,
      paymentType: productOrder.paymentType,
      customerId: productOrder.customerId,
      deliveryAddressId: productOrder.deliveryAddressId,
      productId: productOrder.productId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const productOrder = this.createFromForm();
    if (productOrder.id !== undefined) {
      this.subscribeToSaveResponse(this.productOrderService.update(productOrder));
    } else {
      this.subscribeToSaveResponse(this.productOrderService.create(productOrder));
    }
  }

  private createFromForm(): IProductOrder {
    return {
      ...new ProductOrder(),
      id: this.editForm.get(['id']).value,
      created: this.editForm.get(['created']).value != null ? moment(this.editForm.get(['created']).value, DATE_TIME_FORMAT) : undefined,
      updated: this.editForm.get(['updated']).value != null ? moment(this.editForm.get(['updated']).value, DATE_TIME_FORMAT) : undefined,
      state: this.editForm.get(['state']).value,
      deliveryType: this.editForm.get(['deliveryType']).value,
      includeBatteries: this.editForm.get(['includeBatteries']).value,
      description: this.editForm.get(['description']).value,
      deliveryPrice: this.editForm.get(['deliveryPrice']).value,
      paymentType: this.editForm.get(['paymentType']).value,
      customerId: this.editForm.get(['customerId']).value,
      deliveryAddressId: this.editForm.get(['deliveryAddressId']).value,
      productId: this.editForm.get(['productId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrder>>) {
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

  trackCustomerById(index: number, item: ICustomer) {
    return item.id;
  }

  trackLocationById(index: number, item: ILocation) {
    return item.id;
  }

  trackProductById(index: number, item: IProduct) {
    return item.id;
  }
}
