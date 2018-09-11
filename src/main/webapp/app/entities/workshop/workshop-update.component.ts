import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';

@Component({
    selector: 'jhi-workshop-update',
    templateUrl: './workshop-update.component.html'
})
export class WorkshopUpdateComponent implements OnInit {
    isSaving: boolean;
    tags: ITag[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private workshopService: WorkshopService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

    private _workshop: IWorkshop;

    get workshop() {
        return this._workshop;
    }

    set workshop(workshop: IWorkshop) {
        this._workshop = workshop;
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ workshop }) => {
            this.workshop = workshop;
        });
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.workshop.id !== undefined) {
            this.subscribeToSaveResponse(this.workshopService.update(this.workshop));
        } else {
            this.subscribeToSaveResponse(this.workshopService.create(this.workshop));
        }
    }

    trackTagById(index: number, item: ITag) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IWorkshop>>) {
        result.subscribe((res: HttpResponse<IWorkshop>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
