import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';

@Component({
  selector: 'jhi-customer',
  templateUrl: './customer.component.html'
})
export class CustomerComponent implements OnInit, OnDestroy {
  customers: ICustomer[];
  eventSubscriber: Subscription;

  constructor(protected customerService: CustomerService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.customerService.query().subscribe((res: HttpResponse<ICustomer[]>) => {
      this.customers = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInCustomers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: ICustomer) {
    return item.id;
  }

  registerChangeInCustomers() {
    this.eventSubscriber = this.eventManager.subscribe('customerListModification', () => this.loadAll());
  }
}
