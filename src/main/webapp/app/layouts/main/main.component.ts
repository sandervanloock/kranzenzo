import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRouteSnapshot, NavigationEnd, Router } from '@angular/router';

import { JhiLanguageHelper } from 'app/core';
import { Subscription } from 'rxjs/Subscription';
import { NgcCookieConsentService, NgcInitializeEvent, NgcNoCookieLawEvent } from 'ngx-cookieconsent';
import { NgcStatusChangeEvent } from 'ngx-cookieconsent';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit, OnDestroy {
    private popupOpenSubscription: Subscription;
    private popupCloseSubscription: Subscription;
    private initializeSubscription: Subscription;
    private statusChangeSubscription: Subscription;
    private revokeChoiceSubscription: Subscription;
    private noCookieLawSubscription: Subscription;

    constructor(private jhiLanguageHelper: JhiLanguageHelper, private router: Router, private ccService: NgcCookieConsentService) {}

    ngOnInit() {
        this.router.events.subscribe(event => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
        // subscribe to cookieconsent observables to react to main events
        this.popupOpenSubscription = this.ccService.popupOpen$.subscribe(() => {
            // you can use this.ccService.getConfig() to do stuff...
        });

        this.popupCloseSubscription = this.ccService.popupClose$.subscribe(() => {
            // you can use this.ccService.getConfig() to do stuff...
        });

        this.initializeSubscription = this.ccService.initialize$.subscribe((event: NgcInitializeEvent) => {
            // you can use this.ccService.getConfig() to do stuff...
        });

        this.statusChangeSubscription = this.ccService.statusChange$.subscribe((event: NgcStatusChangeEvent) => {
            // you can use this.ccService.getConfig() to do stuff...
        });

        this.revokeChoiceSubscription = this.ccService.revokeChoice$.subscribe(() => {
            // you can use this.ccService.getConfig() to do stuff...
        });

        this.noCookieLawSubscription = this.ccService.noCookieLaw$.subscribe((event: NgcNoCookieLawEvent) => {
            // you can use this.ccService.getConfig() to do stuff...
        });
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = routeSnapshot.data && routeSnapshot.data['pageTitle'] ? routeSnapshot.data['pageTitle'] : 'kranzenzoApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnDestroy(): void {
        this.popupOpenSubscription.unsubscribe();
        this.popupCloseSubscription.unsubscribe();
        this.initializeSubscription.unsubscribe();
        this.statusChangeSubscription.unsubscribe();
        this.revokeChoiceSubscription.unsubscribe();
        this.noCookieLawSubscription.unsubscribe();
    }
}
