package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.nao.ApplyPosture;
import de.fhg.iais.roberta.syntax.action.nao.Blink;
import de.fhg.iais.roberta.syntax.action.nao.LedOff;
import de.fhg.iais.roberta.syntax.action.nao.LedReset;
import de.fhg.iais.roberta.syntax.action.nao.LookAt;
import de.fhg.iais.roberta.syntax.action.nao.PartialStiffnessOff;
import de.fhg.iais.roberta.syntax.action.nao.PartialStiffnessOn;
import de.fhg.iais.roberta.syntax.action.nao.PointAt;
import de.fhg.iais.roberta.syntax.action.nao.RandomEyesDuration;
import de.fhg.iais.roberta.syntax.action.nao.RastaDuration;
import de.fhg.iais.roberta.syntax.action.nao.SetEarIntensity;
import de.fhg.iais.roberta.syntax.action.nao.SetEyeColor;
import de.fhg.iais.roberta.syntax.action.nao.SetLanguage;
import de.fhg.iais.roberta.syntax.action.nao.SetVolume;
import de.fhg.iais.roberta.syntax.action.nao.SitDown;
import de.fhg.iais.roberta.syntax.action.nao.StandUp;
import de.fhg.iais.roberta.syntax.action.nao.StiffnessOff;
import de.fhg.iais.roberta.syntax.action.nao.StiffnessOn;
import de.fhg.iais.roberta.syntax.action.nao.Stop;
import de.fhg.iais.roberta.syntax.action.nao.TaiChi;
import de.fhg.iais.roberta.syntax.action.nao.TurnDegrees;
import de.fhg.iais.roberta.syntax.action.nao.WalkDistance;
import de.fhg.iais.roberta.syntax.action.nao.WalkTo;
import de.fhg.iais.roberta.syntax.action.nao.Wave;
import de.fhg.iais.roberta.syntax.action.nao.WipeForehead;

/**
 * Interface to be used with the visitor pattern to traverse an AST (and generate code, e.g.).
 */
public interface NaoAstVisitor<V> extends AstVisitor<V> {
    /**
     * visit a {@link WalkDistance}.
     *
     * @param walkDistance phrase to be visited
     */
    V visitWalkDistance(WalkDistance<V> walkDistance);

    /**
     * visit a {@link TurnDegrees}.
     *
     * @param turnDegrees phrase to be visited
     */
    V visitTurnDegrees(TurnDegrees<V> turnDegrees);

    /**
     * visit a {@link WalkTo}.
     *
     * @param walkTo phrase to be visited
     */
    V visitWalkTo(WalkTo<V> walkTo);

    /**
     * visit a {@link Stop}.
     *
     * @param stop phrase to be visited
     */
    V visitStop(Stop<V> stop);

    /**
     * visit a {@link StandUp}.
     *
     * @param stand up phrase to be visited
     */
    V visitStandUp(StandUp<V> standUp);

    /**
     * visit a {@link SitDown}.
     *
     * @param sit down phrase to be visited
     */
    V visitSitDown(SitDown<V> sitDown);

    /**
     * visit a {@link TaiChi}.
     *
     * @param tai chi phrase to be visited
     */
    V visitTaiChi(TaiChi<V> taiChi);

    /**
     * visit a {@link Wave}.
     *
     * @param wave phrase to be visited
     */
    V visitWave(Wave<V> wave);

    /**
     * visit a {@link WipeForehead}.
     *
     * @param wipe forehead phrase to be visited
     */
    V visitWipeForehead(WipeForehead<V> wipeForehead);

    /**
     * visit a {@link ApplyPosture}.
     *
     * @param apply posture phrase to be visited
     */
    V visitApplyPosture(ApplyPosture<V> applyPosture);

    /**
     * visit a {@link StiffnessOn}.
     *
     * @param stiffness on phrase to be visited
     */
    V visitStiffnessOn(StiffnessOn<V> stiffnessOn);

    /**
     * visit a {@link StiffnessOff}.
     *
     * @param stiffness off phrase to be visited
     */
    V visitStiffnessOff(StiffnessOff<V> stiffnessOff);

    /**
     * visit a {@link lookAt}.
     *
     * @param look at phrase to be visited
     */
    V visitLookAt(LookAt<V> lookAt);

    /**
     * visit a {@link PointAt}.
     *
     * @param point at phrase to be visited
     */
    V visitPointAt(PointAt<V> pointAt);

    /**
     * visit a {@link PartialStiffnessOn}.
     *
     * @param partial stiffness on phrase to be visited
     */
    V visitPartialStiffnessOn(PartialStiffnessOn<V> partialstiffnessOn);

    /**
     * visit a {@link PartialStiffnessOff}.
     *
     * @param partial stiffness on phrase to be visited
     */
    V visitPartialStiffnessOff(PartialStiffnessOff<V> partialstiffnessOff);

    /**
     * visit a {@link setVolume}.
     *
     * @param set volume on phrase to be visited
     */
    V visitSetVolume(SetVolume<V> setVolume);

    /**
     * visit a {@link setEyeColor}.
     *
     * @param set eye color phrase to be visited
     */
    V visitSetEyeColor(SetEyeColor<V> setEyeColor);

    /**
     * visit a {@link setEyeColor}.
     *
     * @param set eye color phrase to be visited
     */
    V visitSetEarIntensity(SetEarIntensity<V> setEarIntensity);

    /**
     * visit a {@link blink}.
     *
     * @param blink phrase to be visited
     */
    V visitBlink(Blink<V> blink);

    /**
     * visit a {@link ledOff}.
     *
     * @param led off phrase to be visited
     */
    V visitLedOff(LedOff<V> ledOff);

    /**
     * visit a {@link ledReset}.
     *
     * @param led reset phrase to be visited
     */
    V visitLedReset(LedReset<V> ledReset);

    /**
     * visit a {@link RandomEyesDuration}.
     *
     * @param random eyes duration phrase to be visited
     */
    V visitRandomEyesDuration(RandomEyesDuration<V> randomEyesDuration);

    /**
     * visit a {@link RastaDuration}.
     *
     * @param rast duration phrase to be visited
     */
    V visitRastaDuration(RastaDuration<V> rastaDuration);

    /**
     * visit a {@link SetLanguage}.
     *
     * @param set language phrase to be visited
     */
    V visitSetLanguage(SetLanguage<V> setLanguage);

}
