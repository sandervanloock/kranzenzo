import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IProductOrder } from 'app/shared/model/product-order.model';
import { Principal } from 'app/core';
import { ProductOrderService } from './product-order.service';

@Component({
    selector: 'jhi-product-order',
    templateUrl: './product-order.component.html'
})
export class ProductOrderComponent implements OnInit, OnDestroy {
    productOrders: IProductOrder[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private productOrderService: ProductOrderService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.productOrderService.query().subscribe(
            (res: HttpResponse<IProductOrder[]>) => {
                this.productOrders = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProductOrders();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProductOrder) {
        return item.id;
    }

    registerChangeInProductOrders() {
        this.eventSubscriber = this.eventManager.subscribe('productOrderListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
