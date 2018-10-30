import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IWorkshop } from 'app/shared/model/workshop.model';

@Component({
    selector: 'jhi-workshop-detail',
    templateUrl: './workshop-detail.component.html'
})
export class WorkshopDetailComponent implements OnInit {
    workshop: IWorkshop;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ workshop }) => {
            this.workshop = workshop;
        });
    }

    previousState() {
        window.history.back();
    }
}
