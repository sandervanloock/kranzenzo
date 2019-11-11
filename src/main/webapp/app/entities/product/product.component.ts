import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IProduct } from 'app/shared/model/product.model';
import { ProductService } from './product.service';

@Component({
  selector: 'jhi-product',
  templateUrl: './product.component.html'
})
export class ProductComponent implements OnInit, OnDestroy {
  products: IProduct[];
  eventSubscriber: Subscription;

  constructor(protected productService: ProductService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.productService.query().subscribe((res: HttpResponse<IProduct[]>) => {
      this.products = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInProducts();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProduct) {
    return item.id;
  }

  registerChangeInProducts() {
    this.eventSubscriber = this.eventManager.subscribe('productListModification', () => this.loadAll());
  }
}
