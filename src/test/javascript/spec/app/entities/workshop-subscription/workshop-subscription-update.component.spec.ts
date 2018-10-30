/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopSubscriptionUpdateComponent } from 'app/entities/workshop-subscription/workshop-subscription-update.component';
import { WorkshopSubscriptionService } from 'app/entities/workshop-subscription/workshop-subscription.service';
import { WorkshopSubscription } from 'app/shared/model/workshop-subscription.model';

describe('Component Tests', () => {
    describe('WorkshopSubscription Management Update Component', () => {
        let comp: WorkshopSubscriptionUpdateComponent;
        let fixture: ComponentFixture<WorkshopSubscriptionUpdateComponent>;
        let service: WorkshopSubscriptionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopSubscriptionUpdateComponent]
            })
                .overrideTemplate(WorkshopSubscriptionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WorkshopSubscriptionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkshopSubscriptionService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WorkshopSubscription(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.workshopSubscription = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WorkshopSubscription();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.workshopSubscription = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
