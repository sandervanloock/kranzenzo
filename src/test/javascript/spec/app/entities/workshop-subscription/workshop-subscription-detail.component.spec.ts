/* tslint:disable max-line-length */
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {DatePipe} from '@angular/common';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs/Rx';
import {JhiDataUtils, JhiDateUtils, JhiEventManager} from 'ng-jhipster';
import {KransenzoTestModule} from '../../../test.module';
import {MockActivatedRoute} from '../../../helpers/mock-route.service';
import {WorkshopSubscriptionDetailComponent} from '../../../../../../main/webapp/app/entities/workshop-subscription/workshop-subscription-detail.component';
import {WorkshopSubscriptionService} from '../../../../../../main/webapp/app/entities/workshop-subscription/workshop-subscription.service';
import {WorkshopSubscription} from '../../../../../../main/webapp/app/entities/workshop-subscription/workshop-subscription.model';

describe( 'Component Tests', () => {

    describe( 'WorkshopSubscription Management Detail Component', () => {
        let comp: WorkshopSubscriptionDetailComponent;
        let fixture: ComponentFixture<WorkshopSubscriptionDetailComponent>;
        let service: WorkshopSubscriptionService;

        beforeEach( async( () => {
            TestBed.configureTestingModule( {
                                                imports: [KransenzoTestModule],
                                                declarations: [WorkshopSubscriptionDetailComponent],
                                                providers: [JhiDateUtils, JhiDataUtils, DatePipe, {
                                                    provide: ActivatedRoute, useValue: new MockActivatedRoute( {id: 123} )
                                                }, WorkshopSubscriptionService, JhiEventManager]
                                            } ).overrideTemplate( WorkshopSubscriptionDetailComponent, '' )
                .compileComponents();
        } ) );

        beforeEach( () => {
            fixture = TestBed.createComponent( WorkshopSubscriptionDetailComponent );
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get( WorkshopSubscriptionService );
        } );

        describe( 'OnInit', () => {
            it( 'Should call load all on init', () => {
                // GIVEN

                spyOn( service, 'find' ).and.returnValue( Observable.of( new WorkshopSubscription( 10 ) ) );

                // WHEN
                comp.ngOnInit();

                // THEN
                expect( service.find ).toHaveBeenCalledWith( 123 );
                expect( comp.workshopSubscription ).toEqual( jasmine.objectContaining( {id: 10} ) );
            } );
        } );
    } );

} );
