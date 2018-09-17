import { Component, OnInit } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';

import { Account, Principal } from 'app/core';
import { IWorkshop, Workshop } from 'app/shared/model/workshop.model';
import { ITag, Tag } from 'app/shared/model/tag.model';
import { TagService } from 'app/entities/tag';
import { WorkshopService } from 'app/entities/workshop';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.css']
})
export class HomeComponent implements OnInit {
    account: Account;
    workshop: Workshop;
    homepageImages: HomepageImage[];
    homepageTags: Tag[];

    constructor(
        private principal: Principal,
        private eventManager: JhiEventManager,
        private tagService: TagService,
        private workshopService: WorkshopService
    ) {}

    ngOnInit() {
        this.principal.identity().then(account => {
            this.account = account;
        });
        this.registerAuthenticationSuccess();
        this.homepageImages = [
            new HomepageImage('https://images.kranzenzo.be/1000x500/home_1.jpg', 'Mijn winkel'),
            new HomepageImage('https://images.kranzenzo.be/1000x500/home_5.jpg', 'Mijn winkel'),
            new HomepageImage('https://images.kranzenzo.be/1000x500/home_6.jpg', 'Mijn winkel'),
            new HomepageImage('https://images.kranzenzo.be/1000x500/1522343524-a504e7a6-0287-4114-92f4-52dcc8570481', 'Mijn winkel'),
            new HomepageImage('https://images.kranzenzo.be/1000x500/home_11.jpg', 'Mijn winkel')
        ];

        this.tagService.query({ homepage: true }).subscribe((data: HttpResponse<ITag[]>) => {
            this.homepageTags = data.body;
        });
        this.workshopService.find(1).subscribe((ws: HttpResponse<IWorkshop>) => {
            this.workshop = ws.body;
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
