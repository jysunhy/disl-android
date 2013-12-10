#ifndef _NETREF_H
#define	_NETREF_H

#include "Common.h"
#include <stdint.h>

// first available object id
static volatile jlong avail_object_id;

// first available class id
static volatile jint avail_class_id;

// ******************* Net reference get/set routines *******************

// should be in sync with NetReference on the server

// format of net reference looks like this (from HIGHEST)
// 1 bit data trans., 1 bit class instance, 23 bits class id, 40 bits object id
// bit field not used because there is no guarantee of alignment

// TODO rename SPEC
// SPEC flag is used to indicate if some additional data for this object where
// transfered to the server

static const uint8_t OBJECT_ID_POS = 0;
static const uint8_t CLASS_ID_POS = 40;
static const uint8_t CLASS_INSTANCE_POS = 62;
static const uint8_t SPEC_POS = 63;

static const uint64_t OBJECT_ID_MASK = 0xFFFFFFFFFFL;
static const uint64_t CLASS_ID_MASK = 0x3FFFFF;
static const uint64_t CLASS_INSTANCE_MASK = 0x1;
static const uint64_t SPEC_MASK = 0x1;

// get bits from "from" with pattern "bit_mask" lowest bit starting on position
// "low_start" (from 0)
static inline uint64_t get_bits(uint64_t from, uint64_t bit_mask,
		uint8_t low_start) {

	// shift it
	uint64_t bits_shifted = from >> low_start;

	// mask it
	return bits_shifted & bit_mask;
}

// set bits "bits" to "to" with pattern "bit_mask" lowest bit starting on
// position "low_start" (from 0)
static inline void set_bits(uint64_t * to, uint64_t bits,
		uint64_t bit_mask, uint8_t low_start) {

	// mask it
	uint64_t bits_len = bits & bit_mask;
	// move it to position
	uint64_t bits_pos = bits_len << low_start;
	// set
	*to |= bits_pos;
}

inline jlong net_ref_get_object_id(jlong net_ref) {

	return get_bits(net_ref, OBJECT_ID_MASK, OBJECT_ID_POS);
}

inline jint net_ref_get_class_id(jlong net_ref) {

	return get_bits(net_ref, CLASS_ID_MASK, CLASS_ID_POS);
}

inline unsigned char net_ref_get_spec(jlong net_ref) {

	return get_bits(net_ref, SPEC_MASK, SPEC_POS);
}

inline unsigned char net_ref_get_class_instance_bit(jlong net_ref) {

	return get_bits(net_ref, CLASS_INSTANCE_MASK, CLASS_INSTANCE_POS);
}

inline void net_ref_set_object_id(jlong * net_ref, jlong object_id) {

	set_bits((uint64_t *)net_ref, object_id, OBJECT_ID_MASK, OBJECT_ID_POS);
}

inline void net_ref_set_class_id(jlong * net_ref, jint class_id) {

	set_bits((uint64_t *)net_ref, class_id, CLASS_ID_MASK, CLASS_ID_POS);
}

inline void net_ref_set_class_instance(jlong * net_ref, unsigned char cibit) {

	set_bits((uint64_t *)net_ref, cibit, CLASS_INSTANCE_MASK, CLASS_INSTANCE_POS);
}

inline void net_ref_set_spec(jlong * net_ref, unsigned char spec) {

	set_bits((uint64_t *)net_ref, spec, SPEC_MASK, SPEC_POS);
}

// does not increment any counter - just sets the values
static jlong _set_net_reference(jlong object_id, jint class_id, unsigned char spec, unsigned char cbit) {

	jlong net_ref = 0;

	net_ref_set_object_id(&net_ref, object_id);
	net_ref_set_class_id(&net_ref, class_id);
	net_ref_set_spec(&net_ref, spec);
	net_ref_set_class_instance(&net_ref, cbit);

	return net_ref;
}

#endif	/* _NETREF_H */
