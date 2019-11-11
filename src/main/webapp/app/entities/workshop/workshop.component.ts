import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';

@Component({
  selector: 'jhi-workshop',
  templateUrl: './workshop.component.html'
})
export class WorkshopComponent implements OnInit, OnDestroy {
  workshops: IWorkshop[];
  eventSubscriber: Subscription;

  constructor(protected workshopService: WorkshopService, protected eventManager: JhiEventManager) {}

  loadAll() {
    this.workshopService.query().subscribe((res: HttpResponse<IWorkshop[]>) => {
      this.workshops = res.body;
    });
  }

  ngOnInit() {
    this.loadAll();
    this.registerChangeInWorkshops();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IWorkshop) {
    return item.id;
  }

  registerChangeInWorkshops() {
    this.eventSubscriber = this.eventManager.subscribe('workshopListModification', () => this.loadAll());
  }
}
