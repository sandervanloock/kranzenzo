import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IWorkshop } from 'app/shared/model/workshop.model';
import { WorkshopService } from './workshop.service';
import { ITag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { FileUploader } from 'ng2-file-upload/ng2-file-upload';
import { IImage, Image } from 'app/shared/model/image.model';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { S3ImageResizePipe } from 'app/shared/util/s3-image-resize.pipe';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import moment = require('moment');
import { ImageUploadComponent } from 'app/shared/image/image-upload/image-upload.component';

@Component({
    selector: 'jhi-workshop-update',
    templateUrl: './workshop-update.component.html',
    styleUrls: ['./workshop-update.component.css']
})
export class WorkshopUpdateComponent implements OnInit {
    isSaving: boolean;
    tags: ITag[];
    workshopDates: string[] = [];
    @ViewChild(ImageUploadComponent) private imageUpload: ImageUploadComponent;
    private _workshop: IWorkshop;

    constructor(
        private jhiAlertService: JhiAlertService,
        private workshopService: WorkshopService,
        private tagService: TagService,
        private activatedRoute: ActivatedRoute
    ) {}

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
        if (this.workshop) {
            if (!this.workshop.dates) {
                this.workshop.dates = [];
            }
            this.workshop.dates.forEach(workshopDate => {
                this.workshopDates.push(moment(workshopDate.date, DATE_TIME_FORMAT).format(DATE_TIME_FORMAT));
            });
        }
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.workshop.images = this.imageUpload.images;
        this.isSaving = true;
        this.workshopDates.forEach((workshopDate, i) => {
            if (i < this.workshop.dates.length) {
                this.workshop.dates[i].date = moment(workshopDate, DATE_TIME_FORMAT);
            } else {
                this.workshop.dates.push(new WorkshopDate(null, moment(workshopDate, DATE_TIME_FORMAT)));
            }
        });
        if (this.workshop.id !== undefined) {
            this.subscribeToSaveResponse(this.workshopService.update(this.workshop));
        } else {
            this.subscribeToSaveResponse(this.workshopService.create(this.workshop));
        }
    }

    addWorkshopDate() {
        this.workshopDates.push(moment().format(DATE_TIME_FORMAT));
    }

    removeWorkshopDate(index) {
        this.workshopDates.splice(index, 1);
        this.workshop.dates.splice(index, 1);
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
