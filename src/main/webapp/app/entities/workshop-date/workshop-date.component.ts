import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';

@Component({
  selector: 'jhi-workshop-date',
  templateUrl: './workshop-date.component.html'
})
export class WorkshopDateComponent implements OnInit, OnDestroy {
  workshopDates: IWorkshopDate[];
  eventSubscriber: Subscription;

  constructor(protected workshopDateService: WorkshopDateService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.workshopDateService.query().subscribe((res: HttpResponse<IWorkshopDate[]>) => {
      this.workshopDates = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInWorkshopDates();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorkshopDate) {
    return item.id;
  }

  registerChangeInWorkshopDates() {
    this.eventSubscriber = this.eventManager.subscribe('workshopDateListModification', () => this.loadAll());
  }
}
