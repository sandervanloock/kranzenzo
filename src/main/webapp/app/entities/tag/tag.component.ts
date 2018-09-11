import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';

import { ITag } from 'app/shared/model/tag.model';
import { Principal } from 'app/core';
import { TagService } from './tag.service';

@Component({
    selector: 'jhi-tag',
    templateUrl: './tag.component.html'
})
export class TagComponent implements OnInit, OnDestroy {
    tags: ITag[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private tagService: TagService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {}

    loadAll() {
        this.tagService.query().subscribe(
            (res: HttpResponse<ITag[]>) => {
                this.tags = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInTags();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: ITag) {
        return item.id;
    }

    registerChangeInTags() {
        this.eventSubscriber = this.eventManager.subscribe('tagListModification', response => this.loadAll());
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
