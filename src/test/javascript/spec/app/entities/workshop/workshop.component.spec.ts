/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { KranzenzoTestModule } from '../../../test.module';
import { WorkshopComponent } from 'app/entities/workshop/workshop.component';
import { WorkshopService } from 'app/entities/workshop/workshop.service';
import { Workshop } from 'app/shared/model/workshop.model';

describe('Component Tests', () => {
    describe('Workshop Management Component', () => {
        let comp: WorkshopComponent;
        let fixture: ComponentFixture<WorkshopComponent>;
        let service: WorkshopService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [KranzenzoTestModule],
                declarations: [WorkshopComponent],
                providers: []
            })
                .overrideTemplate(WorkshopComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(WorkshopComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WorkshopService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Workshop(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.workshops[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
