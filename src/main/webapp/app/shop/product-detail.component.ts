import { Component, OnDestroy, OnInit } from '@angular/core';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from 'app/entities/product';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-product-detail',
    templateUrl: './product-detail.component.html',
    styleUrls: ['product-detail.css']
})
export class ProductDetailComponent implements OnInit, OnDestroy {
    product: IProduct;
    private subscription: Subscription;
    private eventSubscriber: Subscription;
    submittedAlert: any;

    constructor(
        private eventManager: JhiEventManager,
        private alertService: JhiAlertService,
        private productService: ProductService,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.subscription = this.route.params.subscribe(params => {
            this.load(params['id']);
        });
        this.registerChangeInProducts();
    }

    load(id) {
        this.productService.find(id).subscribe((product: HttpResponse<IProduct>) => {
            this.product = product.body;
        });
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInProducts() {
        this.eventSubscriber = this.eventManager.subscribe('productListModification', response => this.load(this.product.id));
        this.eventManager.subscribe('productOrderCompleted', response => this.setSubmittedAlert(response.content));
    }

    private setSubmittedAlert(props) {
        const alert = this.alertService.addAlert(
            {
                type: props.type,
                msg: props.msg,
                params: {},
                timeout: 5000,
                toast: true
            },
            []
        );
        this.submittedAlert = alert;
        window.scrollTo(0, 0);
    }
}
