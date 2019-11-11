import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

@Component({
  selector: 'jhi-workshop-subscription-detail',
  templateUrl: './workshop-subscription-detail.component.html'
})
export class WorkshopSubscriptionDetailComponent implements OnInit {
  workshopSubscription: IWorkshopSubscription;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ workshopSubscription }) => {
      this.workshopSubscription = workshopSubscription;
    });
  }

  previousState() {
    window.history.back();
  }
}
