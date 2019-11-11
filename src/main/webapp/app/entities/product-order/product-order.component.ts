import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IProductOrder } from 'app/shared/model/product-order.model';
import { ProductOrderService } from './product-order.service';

@Component({
  selector: 'jhi-product-order',
  templateUrl: './product-order.component.html'
})
export class ProductOrderComponent implements OnInit, OnDestroy {
  productOrders: IProductOrder[];
  eventSubscriber: Subscription;

  constructor(protected productOrderService: ProductOrderService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.productOrderService.query().subscribe((res: HttpResponse<IProductOrder[]>) => {
      this.productOrders = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInProductOrders();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProductOrder) {
    return item.id;
  }

  registerChangeInProductOrders() {
    this.eventSubscriber = this.eventManager.subscribe('productOrderListModification', () => this.loadAll());
  }
}
