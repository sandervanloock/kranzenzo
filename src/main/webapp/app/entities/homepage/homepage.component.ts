import { Component, OnInit } from '@angular/core';
import { HomepageSetting, IHomepageSetting } from 'app/shared/model/homepagesettings.model';
import { HomepageService } from 'app/entities/homepage/homepage.service';
import { IImage } from 'app/shared/model/image.model';
import { Router } from '@angular/router';

@Component({
    selector: 'jhi-homepage',
    templateUrl: './homepage.component.html',
    styles: []
})
export class HomepageComponent implements OnInit {
    images: IImage[];
    private isSaving = false;
    private _homepageSettings: IHomepageSetting;

    constructor(private homepageService: HomepageService, private router: Router) {}

    get homepageSettings(): IHomepageSetting {
        return this._homepageSettings;
    }

    set homepageSettings(value: IHomepageSetting) {
        this._homepageSettings = value;
    }

    ngOnInit() {
        this.homepageService.query().subscribe(
            hs => {
                this.initializeSettings(hs);
            },
            () => this.initializeSettings(null)
        );
    }

    save() {
        this.isSaving = true;
        if (this.images) {
            this.homepageSettings.image = this.images.length ? this.images[0] : null;
        }
        this.homepageService.create(this._homepageSettings).subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
    }

    private initializeSettings(hs) {
        if (hs && hs.body) {
            this.homepageSettings = hs.body;
        } else {
            this.homepageSettings = new HomepageSetting();
        }
        this.images = this.homepageSettings.image ? [this.homepageSettings.image] : [];
    }

    private gotoHome() {
        this.router.navigateByUrl('/');
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.gotoHome();
    }

    private onSaveError() {
        this.isSaving = false;
    }
}
