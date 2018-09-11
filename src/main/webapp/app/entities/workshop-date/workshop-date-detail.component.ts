import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';

@Component({
    selector: 'jhi-workshop-date-detail',
    templateUrl: './workshop-date-detail.component.html'
})
export class WorkshopDateDetailComponent implements OnInit {
    workshopDate: IWorkshopDate;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ workshopDate }) => {
            this.workshopDate = workshopDate;
        });
    }

    previousState() {
        window.history.back();
    }
}
