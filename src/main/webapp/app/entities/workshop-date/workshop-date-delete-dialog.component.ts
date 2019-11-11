import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IWorkshopDate } from 'app/shared/model/workshop-date.model';
import { WorkshopDateService } from './workshop-date.service';

@Component({
  selector: 'jhi-workshop-date-delete-dialog',
  templateUrl: './workshop-date-delete-dialog.component.html'
})
export class WorkshopDateDeleteDialogComponent {
  workshopDate: IWorkshopDate;

  constructor(
    protected workshopDateService: WorkshopDateService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.workshopDateService.delete(id).subscribe(() => {
      this.eventManager.broadcast({
        name: 'workshopDateListModification',
        content: 'Deleted an workshopDate'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-workshop-date-delete-popup',
  template: ''
})
export class WorkshopDateDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ workshopDate }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(WorkshopDateDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.workshopDate = workshopDate;
        this.ngbModalRef.result.then(
          () => {
            this.router.navigate(['/workshop-date', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          () => {
            this.router.navigate(['/workshop-date', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
