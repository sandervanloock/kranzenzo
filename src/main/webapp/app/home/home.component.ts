import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Account, Principal } from 'app/core';
import { IWorkshop, Workshop } from 'app/shared/model/workshop.model';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { WorkshopService } from 'app/entities/workshop';
import { HttpResponse } from '@angular/common/http';
import { HomepageService } from 'app/entities/homepage/homepage.service';
import { IHomepageSetting } from 'app/shared/model/homepagesettings.model';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    workshops: Workshop[] = [];
    homepageSettings: IHomepageSetting;
    homepageTags: Tag[];

    constructor(
        private principal: Principal,
        private eventManager: JhiEventManager,
        private tagService: TagService,
        private workshopService: WorkshopService,
        private homepageSettingsService: HomepageService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();

        this.homepageSettingsService.query().subscribe(hs => {
            if (hs.body) {
                this.homepageSettings = hs.body;
            }
        });
        this.tagService.query({ homepage: true }).subscribe((data: HttpResponse<ITag[]>) => {
            this.homepageTags = data.body;
        });
        this.workshopService.getHomepageWorkshops().subscribe((ws: HttpResponse<IWorkshop[]>) => {
            this.workshops = ws.body;
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', message => {
            this.principal.identity().then(account => {
                this.account = account;
            });
        });
    }
}

class HomepageImage {
    constructor(public url: string, public caption?: string, public subcaption?: string) {}
}
