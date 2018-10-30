/* tslint:disable max-line-length */
import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopDateUpdateComponent } from 'app/entities/workshop-date/workshop-date-update.component';
import { WorkshopDateService } from 'app/entities/workshop-date/workshop-date.service';
import { WorkshopDate } from 'app/shared/model/workshop-date.model';

describe('Component Tests', () => {
    describe('WorkshopDate Management Update Component', () => {
        let comp: WorkshopDateUpdateComponent;
        let fixture: ComponentFixture<WorkshopDateUpdateComponent>;
        let service: WorkshopDateService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopDateUpdateComponent]
            })
                .overrideTemplate(WorkshopDateUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WorkshopDateUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkshopDateService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new WorkshopDate(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.workshopDate = entity;
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
                    const entity = new WorkshopDate();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.workshopDate = entity;
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
