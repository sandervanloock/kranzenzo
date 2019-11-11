import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';
import { WorkshopSubscriptionService } from './workshop-subscription.service';

@Component({
  selector: 'jhi-workshop-subscription',
  templateUrl: './workshop-subscription.component.html'
})
export class WorkshopSubscriptionComponent implements OnInit, OnDestroy {
  workshopSubscriptions: IWorkshopSubscription[];
  eventSubscriber: Subscription;

  constructor(protected workshopSubscriptionService: WorkshopSubscriptionService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.workshopSubscriptionService.query().subscribe((res: HttpResponse<IWorkshopSubscription[]>) => {
      this.workshopSubscriptions = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInWorkshopSubscriptions();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorkshopSubscription) {
    return item.id;
  }

  registerChangeInWorkshopSubscriptions() {
    this.eventSubscriber = this.eventManager.subscribe('workshopSubscriptionListModification', () => this.loadAll());
  }
}
