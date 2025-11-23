package com.stitch.grocerly.reprository;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
}

// Loome repository liidese BrandEntity jaoks
// <BrandEntity, Integer> tähendab:
//   - BrandEntity = millise tabeliga töötame
//   - Integer = mis tüüpi on primaarvõti (@Id väli BrandEntity klassis)
