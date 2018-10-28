import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { IProduct } from 'app/shared/model/product.model';
import { Principal } from 'app/core';
import { ProductService } from './product.service';

@Component({
    selector: 'jhi-product',
    templateUrl: './product.component.html'
})
export class ProductComponent implements OnInit, OnDestroy {
    products: IProduct[];
    productsFiltered: IProduct[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private productService: ProductService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        if (this.currentSearch) {
            this.productsFiltered = this.products.filter((p: IProduct) => {
                return (
                    (p.name != null && p.name.toLowerCase().indexOf(this.currentSearch.toLowerCase()) !== -1) ||
                    (p.description != null && p.description.toLowerCase().indexOf(this.currentSearch.toLowerCase()) !== -1)
                );
            });
            return;
        }
        this.productService.query().subscribe(
            (res: HttpResponse<IProduct[]>) => {
                this.productsFiltered = res.body;
                this.products = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInProducts();
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IProduct) {
        return item.id;
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe('productListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
