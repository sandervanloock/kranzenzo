import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IProductOrder } from 'app/shared/model/product-order.model';
import { ProductOrderService } from './product-order.service';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from 'app/entities/customer';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';

@Component({
    selector: 'jhi-product-order-update',
    templateUrl: './product-order-update.component.html'
})
export class ProductOrderUpdateComponent implements OnInit {
    isSaving: boolean;
    customers: ICustomer[];
    deliveryaddresses: ILocation[];
    products: IProduct[];
    created: string;
    updated: string;

    constructor(
        private jhiAlertService: JhiAlertService,
        private productOrderService: ProductOrderService,
        private customerService: CustomerService,
        private locationService: LocationService,
        private productService: ProductService,
        private activatedRoute: ActivatedRoute
    ) {}

    private _productOrder: IProductOrder;

    get productOrder() {
        return this._productOrder;
    }

    set productOrder(productOrder: IProductOrder) {
        this._productOrder = productOrder;
        this.created = moment(productOrder.created).format(DATE_TIME_FORMAT);
        this.updated = moment(productOrder.updated).format(DATE_TIME_FORMAT);
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ productOrder }) => {
            this.productOrder = productOrder;
        });
        this.customerService.query().subscribe(
            (res: HttpResponse<ICustomer[]>) => {
                this.customers = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.locationService.query({ filter: 'productorder-is-null' }).subscribe(
            (res: HttpResponse<ILocation[]>) => {
                if (!this.productOrder.deliveryAddressId) {
                    this.deliveryaddresses = res.body;
                } else {
                    this.locationService.find(this.productOrder.deliveryAddressId).subscribe(
                        (subRes: HttpResponse<ILocation>) => {
                            this.deliveryaddresses = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.productService.query().subscribe(
            (res: HttpResponse<IProduct[]>) => {
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.productOrder.created = moment(this.created, DATE_TIME_FORMAT);
        this.productOrder.updated = moment(this.updated, DATE_TIME_FORMAT);
        if (this.productOrder.id !== undefined) {
            this.subscribeToSaveResponse(this.productOrderService.update(this.productOrder));
        } else {
            this.subscribeToSaveResponse(this.productOrderService.create(this.productOrder));
        }
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<IProductOrder>>) {
        result.subscribe((res: HttpResponse<IProductOrder>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
